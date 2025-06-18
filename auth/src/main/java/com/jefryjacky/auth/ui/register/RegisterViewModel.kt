package com.jefryjacky.auth.ui.register

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.domain.usecase.LoginGoogleUseCase
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.domain.usecase.BaseUseCase
import com.jefryjacky.auth.domain.usecase.RegisterUseCase
import com.jefryjacky.auth.ui.login.LoginRoute
import com.jefyjacky.auth.ui.Event
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val googleSignInUseCase: LoginGoogleUseCase
): BaseViewModel() {

    init{
        addUseCase(registerUseCase)
    }

    val emailErrorEvent = MutableLiveData<Event<String>>()
    val passwordErrorEvent = MutableLiveData<Event<String>>()
    val passwordConfirmationErrorEvent = MutableLiveData<Event<String>>()
    val registerSuccessEvent = MutableLiveData<Event<String>>()
    val registerGoogleSuccessEvent = MutableLiveData<Event<User>>()

    fun register(email:String , password:String, passwordConfirmation:String){
        setLoading(true)
        val input = RegisterUseCase.Input(email, password, passwordConfirmation)
        registerUseCase.execute(input, object : RegisterUseCase.Callback{
            override fun success(email:String) {
                registerSuccessEvent.value = Event(email)
                setLoading(false)
            }

            override fun loading(loading: Boolean) {

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

    fun loginGoogle(token:String){
        setLoading(true)
        val input = LoginGoogleUseCase.Input(token)
        googleSignInUseCase.execute(input, object : LoginGoogleUseCase.Callback{
            override fun success(output: LoginGoogleUseCase.Output) {
                setLoading(false)
                registerGoogleSuccessEvent.value = Event(output.user)
            }

            override fun loading(loading: Boolean) {

            }

            override fun errors(errors: List<BaseUseCase.Error>) {
                setLoading(false)
            }
        })
    }
}