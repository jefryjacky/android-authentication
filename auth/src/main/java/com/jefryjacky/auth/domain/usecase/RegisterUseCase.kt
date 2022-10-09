package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.config.Config
import com.jefryjacky.auth.config.Config.PASSWORD_MIN_LENGTH
import com.jefryjacky.auth.domain.message.Message
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import java.util.regex.Pattern
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers,
    private val messages: Message
): BaseUseCase() {

    fun execute(input : Input, callback: Callback){
        if(canExecute(input, callback)){
            executing = true
            disposables.add(userRepository.register(input.email, input.password)
                .doAfterTerminate { executing=false }
                .observeOn(schedulers.mainThread())
                .subscribe({
                    callback.success(input.email)
                },{
                    checkError(it)
                }))
        }
    }

    private fun canExecute(input : Input, callback: Callback):Boolean{
        if(executing) return false
        val errors = mutableListOf<Error>()
        if(input.email.isNullOrBlank()){
            errors.add(RegisterError.EmailError(messages.messageErrorEmptyEmail()))
        } else if(!Pattern.matches(Config.rfc5352StandardEmailPattern, input.email)){
            errors.add(RegisterError.EmailError(messages.messageErrorInvalidEmail()))
        }

        val specialCharPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        when {
            input.password.isBlank() -> {
                errors.add(RegisterError.PasswordError(messages.messageErrorPasswordEmpty()))
            }
            input.password.length < PASSWORD_MIN_LENGTH -> {
                errors.add(
                    RegisterError.PasswordError(
                        messages.messageErrorPasswordMinLength(
                            PASSWORD_MIN_LENGTH
                        )
                    )
                )
            }
            !specialCharPattern.matcher(input.password).find() -> {
                errors.add(RegisterError.PasswordError(messages.messageErrorPasswordNoSpecialChars()))
            }
            input.password.none { it.isUpperCase() } ->{
                errors.add(RegisterError.PasswordError(messages.messageErrorPasswordNoCapitalChar()))
            }
        }

        when {
            input.retypePassword.isBlank() -> {
                errors.add(RegisterError.PasswordConfirmationError(messages.messageErrorPasswordEmpty()))
            }
            input.password != input.retypePassword -> {
                errors.add(RegisterError.PasswordConfirmationError(messages.passwordAndConfirmationPasswordDoesntMatch()))
            }
        }

        if(errors.isNotEmpty()) {
            callback.errors(errors)
        }
        return errors.isEmpty()
    }

    sealed class RegisterError:Error{
        data class EmailError(val message:String): RegisterError()
        data class PasswordError(val message:String): RegisterError()
        data class PasswordConfirmationError(val message:String): RegisterError()
    }

    data class Input(
        val email:String,
        val password: String,
        val retypePassword:String
    )

    interface Callback:BaseCallback{
        fun success(email:String)
    }
}