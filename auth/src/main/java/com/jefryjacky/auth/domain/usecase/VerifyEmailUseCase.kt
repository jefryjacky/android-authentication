package com.jefryjacky.auth.domain.usecase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.usecase.BaseUseCase
import java.net.URLEncoder
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers
) : BaseUseCase() {

    fun execute(activity:Activity ,input: Input, callback: Callback) {
        val encodedToken = URLEncoder.encode(input.token, "utf-8")
        disposables.add(userRepository.verifyEmail(encodedToken)
            .andThen(userRepository.getUser())
            .observeOn(schedulers.mainThread())
            .subscribe({
                val output = Output(it)
                callback.success(output)
            }, {
                Log.e("jefryjacky", it.stackTraceToString())
                checkError(it)
            })
        )
    }

    interface Callback : BaseUseCase.BaseCallback {
        fun success(output: Output)
    }

    data class Input(
        val token: String
    )

    data class Output(
        val user: User
    )
}