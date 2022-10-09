package com.authentication.router

import android.app.Activity
import com.authentication.ui.home.HomeActivity
import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.ui.login.LoginRoute
import javax.inject.Inject

class LoginRouterImpl @Inject constructor():LoginRoute {
    override fun next(activity: Activity, user: User) {
        HomeActivity.navigate(activity)
    }
}