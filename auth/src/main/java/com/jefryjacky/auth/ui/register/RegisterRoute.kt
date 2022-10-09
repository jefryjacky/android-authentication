package com.jefryjacky.auth.ui.register

import android.app.Activity
import com.jefryjacky.auth.domain.entity.User

interface RegisterRoute {
    fun next(activity:Activity, email:String)
}