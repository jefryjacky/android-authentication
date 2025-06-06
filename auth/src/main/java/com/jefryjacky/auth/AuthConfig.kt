package com.jefryjacky.auth

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

object AuthConfig {
    var SHOW_API_INFO: Boolean = false
    var API = ""
    var API_KEY = ""
    var GOOGLE_AUTH_ID = ""
    var EMAIL_VERIFICATION_BY_LINK = false
    var EMAIL_VERIFICATION_BY_OTP = true
    var FORGOT_PASSWORD_BY_OTP  = false
    var FORGOT_PASSWORD_BY_LINK  = false

    var Theme: @Composable (content: @Composable () -> Unit)-> Unit = {}
    var Typography: Typography = Typography()
}