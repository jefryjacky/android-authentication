package com.jefryjacky.auth.ui.login

import androidx.lifecycle.MutableLiveData
import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.auth.domain.usecase.LoginGoogleUseCase
import com.jefryjacky.core.domain.usecase.BaseUseCase
import com.jefryjacky.auth.domain.usecase.LoginUseCase
import com.jefyjacky.auth.ui.Event
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginGoogleUseCase: LoginGoogleUseCase
): BaseViewModel() {

    init {
        addUseCase(loginUseCase)
        addUseCase(loginGoogleUseCase)
    }

    val emailErrorEvent = MutableLiveData<Event<String>>()
    val passwordErrorEvent = MutableLiveData<Event<String>>()
    val loginSuccessEvent = MutableLiveData<Event<User>>()
    val loginGoogleFailedEvent = MutableLiveData<Event<Any>>()

        fun login(email:String, password:String){
            setLoading(true)
            val input = LoginUseCase.Input(email, password)
            loginUseCase.login(input, object : LoginUseCase.Callback {
                override fun success(output: LoginUseCase.Output) {
                    setLoading(false)
                    loginSuccessEvent.value = Event(output.user)
                }

                override fun errors(errors: List<BaseUseCase.Error>) {
                    setLoading(false)
                    errors.forEach {
                        when(it){
                            is LoginUseCase.LoginError.EMAIL_ERROR -> {
                                emailErrorEvent.value = Event(it.message)
                            }
                            is LoginUseCase.LoginError.PASSWORD_ERROR ->{
                                passwordErrorEvent.value = Event(it.message)
                            }
                        }
                    }
                }
            })
        }

    fun loginGoogle(token:String){
        setLoading(true)
        val input = LoginGoogleUseCase.Input(token)
        loginGoogleUseCase.execute(input, object : LoginGoogleUseCase.Callback{
            override fun success(output: LoginGoogleUseCase.Output) {
                setLoading(false)
                loginSuccessEvent.value = Event(output.user)
            }

            override fun errors(errors: List<BaseUseCase.Error>) {
                setLoading(false)
            }
        })
    }
}