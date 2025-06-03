package com.jefryjacky.auth.ui.emailverificationotp

sealed interface EmailVerificationOtpEvent {
    data class TypeOtpEvent(val otp: String) : EmailVerificationOtpEvent
    object RequestOtpEvent: EmailVerificationOtpEvent
}