package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository:UserRepository,
    private val schedulers: Schedulers
):AuthBaseUseCase(userRepository, schedulers) {

    fun execute(callback:Callback){
            isLogin {
                disposables.add(userRepository.getUser()
                    .observeOn(schedulers.mainThread())
                    .subscribe({
                        callback.success(it)
                    }, {
                        checkError(it)
                    })
                )
            }
    }

    interface Callback: BaseCallback{
        fun success(user:User)
    }
}