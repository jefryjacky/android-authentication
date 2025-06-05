package com.jefryjacky.auth.ui.forgotpasswordbyotp

import com.jefryjacky.auth.domain.usecase.RequestChangePasswordOtpUseCase
import com.jefryjacky.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordByOtpViewModel @Inject constructor(
    private val requestChangePasswordOtpUseCase: RequestChangePasswordOtpUseCase
): BaseViewModel() {

    init {
        addUseCase(requestChangePasswordOtpUseCase)
    }

    private val _state = MutableStateFlow<ForgotPasswordByOtpState>(ForgotPasswordByOtpState())
    val state = _state.asStateFlow()

    fun handleEvent(event: ForgotPasswordByOtpEvent){
        when(event){
            is ForgotPasswordByOtpEvent.TypingEmailEvent -> {
                _state.update {
                    it.copy(email = event.email)
                }
            }
            is ForgotPasswordByOtpEvent.TypingNewPasswordEvent -> {
                _state.update {
                    it.copy(newPassword = event.newPassword)
                }
            }
            is ForgotPasswordByOtpEvent.TypingConfirmPasswordEvent -> {
                _state.update {
                    it.copy(confirmPassword = event.confirmPassword)
                }
            }
            ForgotPasswordByOtpEvent.NextEvent -> {

            }
        }
    }
}