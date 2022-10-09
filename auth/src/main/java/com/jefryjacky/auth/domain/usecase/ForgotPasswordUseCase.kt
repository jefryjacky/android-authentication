package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.config.Config
import com.jefryjacky.auth.domain.message.Message
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.exception.InvetisApiException
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import java.util.regex.Pattern
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers,
    private val messages: Message
):BaseUseCase() {

    fun execute(input:Input, callback:Callback){
        if(canExecute(input, callback)) {
            disposables.add(userRepository.forgotPassword(input.email)
                .observeOn(schedulers.mainThread())
                .subscribe({
                    callback.success()
                }, {
                    if(it is InvetisApiException){
                        if(it.message == "user not found"){
                            val errors = arrayListOf<Error>()
                            errors.add(ForgotPasswordError.EMAIL_ERROR(messages.messageErrorsEmailHaveNotBeenRegitered()))
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
        if(input.email.isBlank()){
            errors.add(ForgotPasswordError.EMAIL_ERROR(messages.messageErrorEmptyEmail()))
        } else if(!Pattern.matches(Config.rfc5352StandardEmailPattern, input.email)){
            errors.add(ForgotPasswordError.EMAIL_ERROR(messages.messageErrorInvalidEmail()))
        }

        if(errors.isNotEmpty()) {
            callback.errors(errors)
        }
        return errors.isEmpty()
    }

    sealed class ForgotPasswordError:Error{
        data class EMAIL_ERROR(val message:String): ForgotPasswordError()
    }

    data class Input(
        val email:String
    )

    interface Callback:BaseCallback{
        fun success()
    }
}