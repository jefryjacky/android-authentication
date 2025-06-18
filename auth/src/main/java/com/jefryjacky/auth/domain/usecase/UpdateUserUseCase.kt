package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.domain.message.Message
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val scheduler: Schedulers,
    private val message: Message
): BaseUseCase(){

    fun execute(input: Input, callback: Callback){
        canExecute(input, callback){
            executing = true
            callback.loading(true)
            val user = User(displayName = input.displayName)
            disposables.add(userRepository.updateUser(user)
                .observeOn(scheduler.mainThread())
                .doFinally {
                    callback.loading(false)
                }
                .subscribe({
                    callback.success()
                },{
                    checkError(it)
                }))
        }
    }

    fun canExecute(input: Input, callback: Callback, execute: ()-> Unit){
        val errors = mutableListOf<Error>()
        if(executing) return
        if(input.displayName.isBlank()){
            errors.add(UpdateUserUseCaseError.DisplayNameError(message.messageErrorEmptyDisplayName()))
        }
        if(errors.isEmpty()){
            execute()
        } else {
            callback.errors(errors)
        }
    }

    interface Callback:BaseCallback{
        fun success()
    }

    sealed class UpdateUserUseCaseError:Error{
        data class DisplayNameError(val message:String): UpdateUserUseCaseError()
    }

    data class Input(
        val displayName: String
    )
}