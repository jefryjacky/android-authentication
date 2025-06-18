package com.authentication.router

import android.app.Activity
import com.authentication.ui.home.HomeActivity
import com.jefryjacky.auth.ui.updateuser.UpdateUserRoute
import javax.inject.Inject

class UpdateUserRouteImpl @Inject constructor(): UpdateUserRoute {
    override fun next(activity: Activity) {
        HomeActivity.navigate(activity)
        activity.finish()
    }
}