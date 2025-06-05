package com.jefryjacky.auth.ui.verifyforgotpasswordbyotp

sealed interface VerifyForgotPasswordByOtpEvent {
    data class TypeOtpEvent(val otp: String) : VerifyForgotPasswordByOtpEvent
    object RequestOtpEvent: VerifyForgotPasswordByOtpEvent
}