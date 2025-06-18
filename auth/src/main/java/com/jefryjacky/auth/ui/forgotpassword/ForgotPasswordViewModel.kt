package com.jefryjacky.auth.ui.forgotpassword

import androidx.lifecycle.MutableLiveData
import com.jefryjacky.auth.domain.usecase.ForgotPasswordUseCase
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.base.Event
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
): BaseViewModel() {

    init {
        addUseCase(forgotPasswordUseCase)
    }

    val successRequestResetEvent = MutableLiveData<Event<Any>>()
    val emailErrorEvent = MutableLiveData<Event<String>>()

    fun forgotPassword(email:String){
        setLoading(true)
        val input = ForgotPasswordUseCase.Input(email)
        forgotPasswordUseCase.execute(input, object :ForgotPasswordUseCase.Callback{
            override fun success() {
                setLoading(false)
                successRequestResetEvent.value = Event(Any())
            }

            override fun loading(loading: Boolean) {

            }

            override fun errors(errors: List<BaseUseCase.Error>) {
                setLoading(false)
                errors.forEach {
                    when(it){
                        is ForgotPasswordUseCase.ForgotPasswordError.EMAIL_ERROR -> {
                            emailErrorEvent.value = Event(it.message)
                        }
                    }
                }
            }

        })
    }
}