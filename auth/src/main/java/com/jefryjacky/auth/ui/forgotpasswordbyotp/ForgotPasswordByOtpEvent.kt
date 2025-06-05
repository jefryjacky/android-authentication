package com.jefryjacky.auth.ui.forgotpasswordbyotp

sealed interface ForgotPasswordByOtpEvent {
    data class TypingEmailEvent(val email:String): ForgotPasswordByOtpEvent
    data class TypingNewPasswordEvent(val newPassword:String): ForgotPasswordByOtpEvent
    data class TypingConfirmPasswordEvent(val confirmPassword:String): ForgotPasswordByOtpEvent
    data object NextEvent: ForgotPasswordByOtpEvent
}