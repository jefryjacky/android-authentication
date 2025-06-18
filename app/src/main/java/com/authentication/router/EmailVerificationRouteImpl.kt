package com.authentication.router

import android.app.Activity
import com.authentication.ui.home.HomeActivity
import com.jefryjacky.auth.ui.emailverification.EmailVerificationRoute
import com.jefryjacky.auth.ui.updateuser.UpdateUserActivity
import javax.inject.Inject

class EmailVerificationRouteImpl @Inject constructor():EmailVerificationRoute {
    override fun next(activity: Activity) {
        UpdateUserActivity.navigate(activity)
        activity.finish()
    }
}