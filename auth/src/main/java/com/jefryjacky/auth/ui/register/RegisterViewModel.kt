package com.jefryjacky.auth.ui.register

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.domain.usecase.BaseUseCase
import com.jefryjacky.auth.domain.usecase.RegisterUseCase
import com.jefyjacky.auth.ui.Event
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val registerRoute:RegisterRoute
): BaseViewModel() {

    init{
        addUseCase(registerUseCase)
    }

    val emailErrorEvent = MutableLiveData<Event<String>>()
    val passwordErrorEvent = MutableLiveData<Event<String>>()
    val passwordConfirmationErrorEvent = MutableLiveData<Event<String>>()
    val registerSuccessEvent = MutableLiveData<Event<String>>()

    fun register(email:String , password:String, passwordConfirmation:String){
        setLoading(true)
        val input = RegisterUseCase.Input(email, password, passwordConfirmation)
        registerUseCase.execute(input, object : RegisterUseCase.Callback{
            override fun success(email:String) {
                registerSuccessEvent.value = Event(email)
                setLoading(false)
            }

            override fun errors(errors: List<BaseUseCase.Error>) {
                errors.forEach {
                    when(it){
                        is RegisterUseCase.RegisterError.EmailError -> emailErrorEvent.value = Event(it.message)
                        is RegisterUseCase.RegisterError.PasswordError -> passwordErrorEvent.value = Event(it.message)
                        is RegisterUseCase.RegisterError.PasswordConfirmationError -> passwordConfirmationErrorEvent.value = Event(it.message)
                    }
                }
                setLoading(false)
            }
        })
    }

    fun navigateNext(activity: Activity, email: String){
        registerRoute.next(activity, email)
    }
}