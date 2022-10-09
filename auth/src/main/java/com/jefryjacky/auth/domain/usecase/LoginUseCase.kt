package com.jefryjacky.auth.domain.usecase


import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.core.domain.exception.InvetisApiException
import com.jefryjacky.auth.domain.message.Message
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers,
    private val messages: Message
): BaseUseCase() {

    fun login(input: Input, callback: Callback){
        if(canExecute(input, callback)) {
            executing = true
            disposables.add(userRepository.login(input.email, input.password)
                .flatMap {
                    userRepository.getUser()
                }
                .doAfterTerminate { executing = false }
                .observeOn(schedulers.mainThread())
                .subscribe({
                    callback.success(Output(it))
                }, {
                    checkError(it)
                })
            )
        }
    }

    private fun canExecute(input: Input, callback: Callback):Boolean{
        if(executing) return false
        val errors = mutableListOf<Error>()
        if(input.email.isBlank()){
            errors.add(LoginError.EMAIL_ERROR(messages.messageErrorEmptyEmail()))
        }
        if(input.password.isBlank()){
            errors.add(LoginError.PASSWORD_ERROR(messages.messageErrorPasswordEmpty()))
        }
        if(errors.isNotEmpty()) {
            callback.errors(errors)
        }
        return errors.isEmpty()
    }

    sealed class LoginError:Error{
        data class EMAIL_ERROR(val message:String): LoginError()
        data class PASSWORD_ERROR(val message:String): LoginError()
    }

    data class Input(
        val email:String,
        val password: String
    )

    interface Callback:BaseCallback{
        fun success(output: Output)
    }

    data class Output(
        val user: User
    )
}