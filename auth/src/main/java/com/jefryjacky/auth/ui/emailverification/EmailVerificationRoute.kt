package com.jefryjacky.auth.ui.emailverification

import android.app.Activity

interface EmailVerificationRoute {
    fun next(activity:Activity)
}