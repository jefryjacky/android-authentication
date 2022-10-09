package com.jefryjacky.auth.ui.login

import android.app.Activity
import com.jefryjacky.auth.domain.entity.User

interface LoginRoute {
    fun next(activity:Activity, user: User)
}