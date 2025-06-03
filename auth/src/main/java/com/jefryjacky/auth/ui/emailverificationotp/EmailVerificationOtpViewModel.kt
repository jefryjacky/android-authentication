package com.jefryjacky.auth.ui.emailverificationotp

import androidx.lifecycle.viewModelScope
import com.jefryjacky.auth.domain.usecase.RequestEmailVerificationOtpUseCase
import com.jefryjacky.auth.domain.usecase.VerifyEmailOtpUseCase
import com.jefryjacky.auth.ui.emailverification.EmailVerificationRoute
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.domain.usecase.BaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailVerificationOtpViewModel @Inject constructor(
    private val requestEmailVerificationOtpUseCase: RequestEmailVerificationOtpUseCase,
    private val verifyEmailOtpUseCase: VerifyEmailOtpUseCase
): BaseViewModel() {

    protected val _successEvent = Channel<Any>()
    val successEvent = _successEvent.receiveAsFlow()

    protected val _hideKeyboardEvent = Channel<Any>()
    val hideKeyboardEvent = _hideKeyboardEvent.receiveAsFlow()

    init {
        addUseCase(requestEmailVerificationOtpUseCase)
        addUseCase(verifyEmailOtpUseCase)
    }

    private val _state = MutableStateFlow(EmailVerificationOtpState())
    val state = _state.asStateFlow()

    fun setEmail(email: String){
        _state.value = _state.value.copy(email = email)
    }

    fun requestOtp(){
        requestEmailVerificationOtpUseCase.execute(state.value.email)
        _state.update {
            it.copy(expire = System.currentTimeMillis() + 60000*3)
        }
    }

    fun handleEvent(event: EmailVerificationOtpEvent) {
        when (event) {
            is EmailVerificationOtpEvent.TypeOtpEvent -> {
                _state.update {
                    it.copy(otp = event.otp, error = "")
                }
                if(event.otp.length == 6){
                    viewModelScope.launch {
                        _hideKeyboardEvent.send(Any())
                    }
                    verifyEmailOtpUseCase.execute(VerifyEmailOtpUseCase.Input(
                        otp = event.otp,
                        email = state.value.email
                    ), callback = object :VerifyEmailOtpUseCase.Callback{
                        override fun success(output: VerifyEmailOtpUseCase.Output) {
                            viewModelScope.launch {
                                _successEvent.send(Any())
                            }
                        }

                        override fun invalidOtp(message: String) {
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

            is EmailVerificationOtpEvent.RequestOtpEvent -> {
                requestOtp()
            }
        }
    }
}