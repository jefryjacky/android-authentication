package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class RequestChangePasswordOtpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers
): BaseUseCase() {

    fun execute(email:String){
        disposables.add(userRepository.requestChangePasswordOtp(email)
            .observeOn(schedulers.mainThread())
            .subscribe({},{
                checkError(it)
            }))
    }
}