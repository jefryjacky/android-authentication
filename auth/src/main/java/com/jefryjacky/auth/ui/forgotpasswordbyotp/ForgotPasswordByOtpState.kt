package com.jefryjacky.auth.ui.forgotpasswordbyotp

data class ForgotPasswordByOtpState(
    val email: String = "",
    val newPassword: String = "",
    val confirmPassword: String=""
)