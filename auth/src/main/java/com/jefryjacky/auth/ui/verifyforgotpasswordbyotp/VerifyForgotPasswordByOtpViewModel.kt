package com.jefryjacky.auth.ui.verifyforgotpasswordbyotp

import androidx.lifecycle.viewModelScope
import com.jefryjacky.auth.domain.usecase.RequestChangePasswordOtpUseCase
import com.jefryjacky.auth.domain.usecase.RequestEmailVerificationOtpUseCase
import com.jefryjacky.auth.domain.usecase.UpdatePasswordByOtpUseCase
import com.jefryjacky.auth.domain.usecase.VerifyEmailOtpUseCase
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.domain.usecase.BaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyForgotPasswordByOtpViewModel @Inject constructor(
    private val requestChangePasswordOtpUseCase: RequestChangePasswordOtpUseCase,
    private val updatePasswordByOtpUseCase: UpdatePasswordByOtpUseCase
): BaseViewModel() {

    init {
        addUseCase(requestChangePasswordOtpUseCase)
        addUseCase(updatePasswordByOtpUseCase)
    }

    var newPassword = ""

    protected val _successEvent = Channel<Any>()
    val successEvent = _successEvent.receiveAsFlow()

    protected val _hideKeyboardEvent = Channel<Any>()
    val hideKeyboardEvent = _hideKeyboardEvent.receiveAsFlow()

    private val _state = MutableStateFlow(VerifyForgotPasswordByOtpState())

    val state = _state.asStateFlow()

    fun setEmail(email: String){
        _state.value = _state.value.copy(email = email)
    }

    fun requestOtp(){
        requestChangePasswordOtpUseCase.execute(state.value.email)
        _state.update {
            it.copy(expire = System.currentTimeMillis() + 60000*3)
        }
    }

    fun handleEvent(event: VerifyForgotPasswordByOtpEvent) {
        when (event) {
            is VerifyForgotPasswordByOtpEvent.TypeOtpEvent -> {
                _state.update {
                    it.copy(otp = event.otp, error = "")
                }
                if(event.otp.length == 6){
                    viewModelScope.launch {
                        _hideKeyboardEvent.send(Any())
                    }
                    updatePasswordByOtpUseCase.execute(UpdatePasswordByOtpUseCase.Input(
                        otp = event.otp,
                        email = state.value.email,
                        password = newPassword,
                    ), callback = object :UpdatePasswordByOtpUseCase.Callback{
                        override fun success() {
                            viewModelScope.launch {
                                _successEvent.send(Any())
                            }
                        }

                        override fun errorInvalidOtp(message: String) {
                            viewModelScope.launch {
                                _errorFlow.send(message)
                            }
                            _state.update {
                                it.copy(error = message)
                            }
                        }

                        override fun loading(loading: Boolean) {
                            setLoading(loading)
                        }

                        override fun errors(errors: List<BaseUseCase.Error>) {

                        }
                    })
                }
            }

            is VerifyForgotPasswordByOtpEvent.RequestOtpEvent -> {
                requestOtp()
            }
        }
    }
}