package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class IsLoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers
):BaseUseCase() {

    fun execute(callback: Callback){
        disposables.add(userRepository.isLogin()
            .observeOn(schedulers.mainThread())
            .subscribe({
                val output = Output(true)
                callback.success(output)
            },{
                if(it is IllegalAccessException){
                    val output = Output(false)
                    callback.success(output)
                } else {
                    checkError(it)
                }
            }))
    }


    interface Callback:BaseCallback{
        fun success(output:Output)
    }

    data class Output(
        val isLogin: Boolean
    )
}