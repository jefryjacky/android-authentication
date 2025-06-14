package com.jefryjacky.auth

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography

object AuthConfig {
    var SHOW_API_INFO: Boolean = false
    var API = ""
    var API_KEY = ""
    var GOOGLE_AUTH_ID = ""
    var EMAIL_VERIFICATION_BY_LINK = false
    var EMAIL_VERIFICATION_BY_OTP = true
    var FORGOT_PASSWORD_BY_OTP  = false
    var FORGOT_PASSWORD_BY_LINK  = false
    var LIGHT_COLOR_SCHEME: ColorScheme? = null
    var DARK_COLOR_SCHEME: ColorScheme? = null
    var Typography: Typography = Typography()
    var DISABLE_DYNAMIC_THEME = false
}