package com.jefryjacky.auth.ui.emailverification

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.jefryjacky.auth.domain.usecase.IsLoginUseCase
import com.jefryjacky.auth.domain.usecase.RequestEmailVerificationUseCase
import com.jefryjacky.auth.domain.usecase.VerifyEmailUseCase
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.base.Event
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class EmailVerificationViewModel @Inject constructor(
    private val requestEmailVerificationUseCase: RequestEmailVerificationUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val isLoginUseCase: IsLoginUseCase,
    private val emailVerificationRoute:EmailVerificationRoute
):BaseViewModel() {

    val successVerifyEmailEvent = MutableLiveData<Event<Any>>()
    val failedVerifyEmailEvent = MutableLiveData<Event<Any>>()
    val navigateToLoginViewEvent = MutableLiveData<Event<Any>>()

    init {
        addUseCase(requestEmailVerificationUseCase)
        addUseCase(verifyEmailUseCase)
        addUseCase(isLoginUseCase)
    }

    fun requestEmailVerification(email:String){
        requestEmailVerificationUseCase.execute(email)
    }

    fun verifyEmail(token:String){
        setLoading(true)
        val input = VerifyEmailUseCase.Input(token)
        verifyEmailUseCase.execute(input, object :VerifyEmailUseCase.Callback{
            override fun success(output: VerifyEmailUseCase.Output) {
                setLoading(false)
                successVerifyEmailEvent.value = Event(Any())
            }

            override fun errors(errors: List<BaseUseCase.Error>) {
                setLoading(false)
                failedVerifyEmailEvent.value = Event(Any())
            }
        })
    }

    fun isLoginUseCase(){
        isLoginUseCase.execute(object :IsLoginUseCase.Callback{
            override fun success(output: IsLoginUseCase.Output) {
                if(!output.isLogin) navigateToLoginViewEvent.value = Event(Any())
            }

            override fun errors(errors: List<BaseUseCase.Error>) {
                navigateToLoginViewEvent.value = Event(Any())
            }
        })
    }

    fun navigateNext(activity: Activity){
        emailVerificationRoute.next(activity)
    }
}