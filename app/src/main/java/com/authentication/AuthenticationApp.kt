package com.authentication

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.authentication.ui.testcompose.ui.theme.Typography
import com.jefryjacky.auth.AuthConfig
import com.jefryjacky.auth.ui.theme.DarkColorScheme
import com.jefryjacky.auth.ui.theme.LightColorScheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AuthenticationApp:Application() {

    override fun onCreate() {
        super.onCreate()
        AuthConfig.SHOW_API_INFO = BuildConfig.FLAVOR == "staging" || BuildConfig.DEBUG
        AuthConfig.API = BuildConfig.AUTH_API
        AuthConfig.API_KEY = BuildConfig.API_KEY
        AuthConfig.GOOGLE_AUTH_ID = BuildConfig.GOOGLE_AUTH_ID
        AuthConfig.EMAIL_VERIFICATION_BY_LINK = false
        AuthConfig.EMAIL_VERIFICATION_BY_OTP = true
        AuthConfig.FORGOT_PASSWORD_BY_OTP = true
        AuthConfig.FORGOT_PASSWORD_BY_LINK = false
        AuthConfig.DARK_COLOR_SCHEME = DarkColorScheme
        AuthConfig.DARK_COLOR_SCHEME = LightColorScheme

        AuthConfig.Typography = Typography
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}