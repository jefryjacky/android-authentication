package com.jefryjacky.auth.ui.verifyforgotpasswordbyotp

data class VerifyForgotPasswordByOtpState(
    val expire:Long = 0,
    val otp: String = "",
    val error: String = "",
    val email: String = ""
)
