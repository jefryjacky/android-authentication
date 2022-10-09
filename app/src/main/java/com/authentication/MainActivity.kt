package com.authentication

import android.os.Bundle
import com.jefryjacky.core.base.BaseActivity
import com.authentication.databinding.ActivityMainBinding
import com.jefryjacky.auth.ui.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WelcomeActivity.navigate(this)
        finish()
    }

    override fun inflate(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

}