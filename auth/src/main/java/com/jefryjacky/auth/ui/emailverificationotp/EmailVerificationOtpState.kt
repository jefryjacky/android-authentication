package com.jefryjacky.auth.ui.emailverificationotp

data class EmailVerificationOtpState(
    val expire:Long = 0,
    val otp: String = "",
    val error: String = "",
    val email: String = ""
)
