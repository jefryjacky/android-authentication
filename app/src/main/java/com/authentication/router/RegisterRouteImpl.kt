package com.authentication.router

import android.app.Activity
import com.jefryjacky.auth.ui.emailverification.EmailVerificationActivity
import com.jefryjacky.auth.ui.register.RegisterRoute
import javax.inject.Inject

class RegisterRouteImpl @Inject constructor():RegisterRoute {
    override fun next(activity: Activity, email: String) {
        EmailVerificationActivity.navigate(activity, email)
    }
}