package com.jefryjacky.auth.ui.changepassword

import androidx.lifecycle.MutableLiveData
import com.jefryjacky.auth.domain.usecase.UpdatePasswordUseCase
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.base.Event
import com.jefryjacky.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
    private val updatePasswordUseCase: UpdatePasswordUseCase,
):BaseViewModel() {

    val successEvent = MutableLiveData<Event<Any>>()
    val currentPasswordErrorEvent = MutableLiveData<Event<String>>()
    val newPasswordErrorEvent = MutableLiveData<Event<String>>()
    val newPasswordConfirmationErrorEvent = MutableLiveData<Event<String>>()

    fun submit(currentPassword:String, newPassword:String, newPasswordConfirmation:String){
        setLoading(true)
        val input = UpdatePasswordUseCase.Input(currentPassword, newPassword, newPasswordConfirmation)
        updatePasswordUseCase.execute(input, object :UpdatePasswordUseCase.Callback{
            override fun success() {
                setLoading(false)
                successEvent.value = Event(Any())
            }

            override fun loading(loading: Boolean) {

            }

            override fun errors(errors: List<BaseUseCase.Error>) {
                setLoading(false)
                errors.forEach {
                    when(it){
                        is UpdatePasswordUseCase.UpdatePasswordError.CurrentPasswordError ->{
                            currentPasswordErrorEvent.value = Event(it.message)
                        }
                        is UpdatePasswordUseCase.UpdatePasswordError.NewPasswordError ->{
                            newPasswordErrorEvent.value = Event(it.message)
                        }
                        is UpdatePasswordUseCase.UpdatePasswordError.NewPasswordConfirmationError ->{
                            newPasswordConfirmationErrorEvent.value = Event(it.message)
                        }
                    }
                }
            }

        })
    }
}