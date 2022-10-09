package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.config.Config
import com.jefryjacky.auth.domain.message.Message
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.exception.InvetisApiException
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import java.net.URLEncoder
import java.util.regex.Pattern
import javax.inject.Inject

class UpdatePasswordByTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers,
    private val messages: Message
):BaseUseCase() {

    fun execute(input:Input, callback: Callback){
        val encodedToken = URLEncoder.encode(input.token, "utf-8")
        if(canExecute(input, callback)){
            executing = true
            disposables.add(userRepository.updatePasswordByToken(input.password, encodedToken)
                .doAfterTerminate { executing = false }
                .observeOn(schedulers.mainThread())
                .subscribe({
                    callback.success()
                },{
                    if(it is InvetisApiException){
                        if(it.code == 403){
                            val errors = mutableListOf<ResetPasswordError>()
                            errors.add(ResetPasswordError.TokenError(messages.messageErrorsResetPasswordLinkInvalid()))
                            callback.errors(errors)
                        }
                    } else {
                        checkError(it)
                    }
                }))
        }
    }

    private fun canExecute(input:Input, callback: Callback):Boolean{
        if(executing) return false
        val errors = mutableListOf<Error>()
        val specialCharPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        when {
            input.password.isBlank() -> {
                errors.add(ResetPasswordError.PasswordError(messages.messageErrorPasswordEmpty()))
            }
            input.password.length < Config.PASSWORD_MIN_LENGTH -> {
                errors.add(
                    ResetPasswordError.PasswordError(
                        messages.messageErrorPasswordMinLength(
                            Config.PASSWORD_MIN_LENGTH
                        )
                    )
                )
            }
            !specialCharPattern.matcher(input.password).find() -> {
                errors.add(ResetPasswordError.PasswordError(messages.messageErrorPasswordNoSpecialChars()))
            }
            input.password.none { it.isUpperCase() } ->{
                errors.add(ResetPasswordError.PasswordError(messages.messageErrorPasswordNoCapitalChar()))
            }
        }

        when {
            input.passwordConfirmation.isBlank() -> {
                errors.add(ResetPasswordError.PasswordConfirmationError(messages.messageErrorPasswordEmpty()))
            }
            input.password != input.passwordConfirmation -> {
                errors.add(ResetPasswordError.PasswordConfirmationError(messages.passwordAndConfirmationPasswordDoesntMatch()))
            }
        }
        if(errors.isNotEmpty()) {
            callback.errors(errors)
        }
        return errors.isEmpty()
    }

    data class Input(
        val token:String,
        val password:String,
        val passwordConfirmation:String
    )

    interface Callback:BaseCallback{
        fun success()
    }

    sealed class ResetPasswordError:Error{
        data class TokenError(val message:String): ResetPasswordError()
        data class PasswordError(val message:String): ResetPasswordError()
        data class PasswordConfirmationError(val message:String): ResetPasswordError()
    }
}