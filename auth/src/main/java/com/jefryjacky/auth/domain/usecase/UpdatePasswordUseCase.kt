package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.config.Config
import com.jefryjacky.auth.domain.message.Message
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.exception.InvetisApiException
import com.jefryjacky.core.domain.scheduler.Schedulers
import java.util.regex.Pattern
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers,
    private val messages: Message
): AuthBaseUseCase(userRepository, schedulers) {
    fun execute(input: Input, callback: Callback){
        if(canExecute(input, callback)){
            executing = true
            disposables.add(userRepository.updatePassword(input.currentPassword, input.newPassword)
                .doAfterTerminate { executing = false }
                .observeOn(schedulers.mainThread())
                .subscribe({
                           callback.success()
                },{
                    if(it is InvetisApiException){
                        if(it.code == 403){
                            val errors = mutableListOf<UpdatePasswordError>()
                            errors.add(UpdatePasswordError.CurrentPasswordError(messages.messageErrorsResetPasswordLinkInvalid()))
                            callback.errors(errors)
                        }
                    } else {
                        checkError(it)
                    }
                })
            )
        }
    }

    private fun canExecute(input: Input, callback: Callback):Boolean{
        if(executing) return false
        val errors = mutableListOf<Error>()
        val specialCharPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        when{
            input.currentPassword.isBlank() -> {
                errors.add(UpdatePasswordError.CurrentPasswordError(messages.messageErrorPasswordEmpty()))
            }
        }
        when {
            input.newPassword.isBlank() -> {
                errors.add(UpdatePasswordError.NewPasswordError(messages.messageErrorPasswordEmpty()))
            }
            input.newPassword.length < Config.PASSWORD_MIN_LENGTH -> {
                errors.add(
                    UpdatePasswordError.NewPasswordError(
                        messages.messageErrorPasswordMinLength(
                            Config.PASSWORD_MIN_LENGTH
                        )
                    )
                )
            }
            !specialCharPattern.matcher(input.newPassword).find() -> {
                errors.add(UpdatePasswordError.NewPasswordError(messages.messageErrorPasswordNoSpecialChars()))
            }
            input.newPassword.none { it.isUpperCase() } ->{
                errors.add(UpdatePasswordError.NewPasswordError(messages.messageErrorPasswordNoCapitalChar()))
            }
        }

        when {
            input.newPasswordConfirmation.isBlank() -> {
                errors.add(UpdatePasswordError.NewPasswordConfirmationError(messages.messageErrorPasswordEmpty()))
            }
            input.newPassword != input.newPasswordConfirmation -> {
                errors.add(UpdatePasswordError.NewPasswordConfirmationError(messages.passwordAndConfirmationPasswordDoesntMatch()))
            }
        }
        if(errors.isNotEmpty()) {
            callback.errors(errors)
        }
        return errors.isEmpty()
    }

    data class Input(
        val currentPassword:String,
        val newPassword:String,
        val newPasswordConfirmation:String
    )

    interface Callback:BaseCallback{
        fun success()
    }

    sealed class UpdatePasswordError:Error{
        data class CurrentPasswordError(val message:String): UpdatePasswordError()
        data class NewPasswordError(val message:String): UpdatePasswordError()
        data class NewPasswordConfirmationError(val message:String): UpdatePasswordError()
    }
}