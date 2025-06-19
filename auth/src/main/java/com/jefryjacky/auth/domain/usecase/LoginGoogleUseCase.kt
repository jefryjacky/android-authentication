package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.core.domain.exception.InvetisApiException
import com.jefryjacky.auth.domain.message.Message
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class LoginGoogleUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers,
    private val messages: Message
): BaseUseCase() {

    fun execute(input: Input, callback: Callback){
        if(canExecute(input, callback)) {
            disposables.add(
                userRepository.loginGoogle(input.token)
                    .flatMap {
                        userRepository.getUser()
                    }
                    .doAfterTerminate {
                        executing = false
                    }.observeOn(schedulers.mainThread())
                    .subscribe({
                        val output = Output(it)
                        callback.success(output)
                    }, {
                        checkError(it)
                    })
            )
        }
    }

    private fun canExecute(input: Input, callback: Callback):Boolean{
        if(input.token.isNullOrBlank()){
            callback.errors(listOf(CommonError.UNAUTHORIZED_ERROR))
            return false
        }
        return !executing
    }

    interface Callback:BaseCallback{
        fun success(output: Output)
    }

    data class Output(
        val user:User
    )

    data class Input(
        val token:String
    )


}