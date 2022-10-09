package com.jefryjacky.auth.ui.resetpassword

import androidx.lifecycle.MutableLiveData
import com.jefryjacky.auth.domain.usecase.UpdatePasswordByTokenUseCase
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.domain.usecase.BaseUseCase
import com.jefyjacky.auth.ui.Event
import javax.inject.Inject

class ResetPasswordViewModel @Inject constructor(
    private val updatePasswordByTokenUseCase:UpdatePasswordByTokenUseCase
):BaseViewModel() {

    val passwordErrorEvent = MutableLiveData<Event<String>>()
    val passwordConfirmationErrorEvent = MutableLiveData<Event<String>>()
    val errorEvent = MutableLiveData<Event<String>>()
    val successEvent = MutableLiveData<Event<Any>>()

    init {
        addUseCase(updatePasswordByTokenUseCase)
    }

    fun submit(token:String, password:String, confirmationPassword:String){
        setLoading(true)
        val input = UpdatePasswordByTokenUseCase.Input(
            password = password,
            passwordConfirmation = confirmationPassword,
            token = token
        )
        updatePasswordByTokenUseCase.execute(input, object: UpdatePasswordByTokenUseCase.Callback{
            override fun success() {
                successEvent.value = Event(Any())
            }

            override fun errors(errors: List<BaseUseCase.Error>) {
                errors.forEach {
                    when(it){
                        is UpdatePasswordByTokenUseCase.ResetPasswordError.PasswordError -> passwordErrorEvent.value = Event(it.message)
                        is UpdatePasswordByTokenUseCase.ResetPasswordError.PasswordConfirmationError -> passwordConfirmationErrorEvent.value = Event(it.message)
                        is UpdatePasswordByTokenUseCase.ResetPasswordError.TokenError -> errorEvent.value = Event(it.message)
                    }
                }
                setLoading(false)
            }

        })
    }
}