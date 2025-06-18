package com.authentication.router

import android.app.Activity
import com.jefryjacky.auth.AuthConfig
import com.jefryjacky.auth.ui.emailverification.EmailVerificationActivity
import com.jefryjacky.auth.ui.emailverificationotp.EmailVerificationOtpActivity
import com.jefryjacky.auth.ui.register.RegisterRoute
import com.jefryjacky.auth.ui.updateuser.UpdateUserActivity
import javax.inject.Inject

class RegisterRouteImpl @Inject constructor():RegisterRoute {
    override fun next(activity: Activity, email: String) {
        if(AuthConfig.EMAIL_VERIFICATION_BY_OTP){
            EmailVerificationOtpActivity.navigate(activity, email)
        } else if(AuthConfig.EMAIL_VERIFICATION_BY_LINK){
            EmailVerificationActivity.navigate(activity, email)
        }
        activity.finish()
    }

    override fun nextGoogleLogin(activity: Activity) {
        UpdateUserActivity.navigate(activity)
    }
}