package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.exception.InvetisApiException
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class VerifyEmailOtpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers
): BaseUseCase() {

    fun execute(input: Input, callback: Callback){
        disposables.add(userRepository.verifyEmailOtp(input.email, input.otp)
            .doOnSubscribe {
                callback.loading(true)
            }
            .andThen(userRepository.getUser())
            .observeOn(schedulers.mainThread())
            .subscribe({
                callback.loading(false)
                val output = Output(it)
                callback.success(output)
            },{
                if(it is InvetisApiException){
                    if(it.code == 403){
                        callback.loading(false)
                        callback.invalidOtp(it.message?:"invalid otp")
                        return@subscribe
                    }
                }
                checkError(it)
            }))
    }

    interface Callback : BaseUseCase.BaseCallback {
        fun success(output: Output)
        fun invalidOtp(message: String)
        fun loading(loading: Boolean)
    }

    data class Input(
        val email: String,
        val otp: String
    )

    data class Output(
        val user: User
    )
}