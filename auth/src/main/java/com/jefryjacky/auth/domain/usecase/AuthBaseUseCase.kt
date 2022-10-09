package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

abstract class AuthBaseUseCase constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers
):BaseUseCase(){

    protected fun isLogin(callback:()->Unit){
        disposables.add(userRepository.isLogin()
            .observeOn(schedulers.mainThread())
            .subscribe({ userToken->
                if(userToken.expiredDate > System.currentTimeMillis() + (20 * 60 * 1000)){
                    callback.invoke()
                } else {
                    refreshToken(callback, userToken.refreshToken)
                }
            },{
                if(it is IllegalAccessException){
                    callBack.invoke(CommonError.UNAUTHORIZED_ERROR)
                }
            }))
    }

    private fun refreshToken(callback:()->Unit, refreshToken:String){
        disposables.add(userRepository.refreshToken(refreshToken)
            .observeOn(schedulers.mainThread())
            .subscribe({
                callback.invoke()
            },{
                checkError(it)
            }))
    }
}