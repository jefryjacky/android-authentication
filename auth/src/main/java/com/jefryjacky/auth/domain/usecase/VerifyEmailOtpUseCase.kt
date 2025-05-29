package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class VerifyEmailOtpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers
): BaseUseCase() {

    fun execute(input: Input, callback: Callback){
        disposables.add(userRepository.verifyEmailOtp(input.email, input.otp)
            .andThen(userRepository.getUser())
            .observeOn(schedulers.mainThread())
            .subscribe({
                val output = Output(it)
                callback.success(output)
            },{
                checkError(it)
            }))
    }

    interface Callback : BaseUseCase.BaseCallback {
        fun success(output: Output)
    }

    data class Input(
        val email: String,
        val otp: String
    )

    data class Output(
        val user: User
    )
}