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

    var Theme: AuthThemeProvider? = null
    var Typography: Typography = Typography()
}

interface AuthThemeProvider {
    @Composable
    fun ApplyTheme(content: @Composable () -> Unit)
}