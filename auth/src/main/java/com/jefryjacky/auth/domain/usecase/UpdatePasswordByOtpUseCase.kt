package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.exception.InvetisApiException
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class UpdatePasswordByOtpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers
): BaseUseCase() {

    fun execute(input: Input, callback: Callback){
        if(!executing) {
            disposables.add(
                userRepository.updatePasswordByOtp(input.email, input.password, input.otp)
                    .doOnSubscribe {
                        executing = true
                        callback.loading(true)
                    }
                .doFinally { executing = false }
                .observeOn(schedulers.mainThread())
                .subscribe({
                    callback.loading(false)
                    callback.success()
                }, {
                    if (it is InvetisApiException) {
                        if (it.code == 403) {
                            callback.loading(false)
                            callback.errorInvalidOtp(it.message?:"Invalid OTP")
                            return@subscribe
                        }
                    }
                    checkError(it)
                })
            )
        }
    }

    interface Callback:BaseCallback{
        fun success()
        fun errorInvalidOtp(message: String)
    }

    data class Input(
        val email: String,
        val otp:String,
        val password:String
    )
}