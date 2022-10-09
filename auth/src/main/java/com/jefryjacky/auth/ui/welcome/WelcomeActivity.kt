package com.jefryjacky.auth.ui.welcome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jefryjacky.auth.databinding.ActivityWelcomeBinding
import com.jefryjacky.core.base.BaseActivity
import com.jefryjacky.auth.ui.login.LoginActivity
import com.jefryjacky.auth.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity: BaseActivity<ActivityWelcomeBinding>() {

    override fun inflate(): ActivityWelcomeBinding {
        return ActivityWelcomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initClickListener()
    }

    private fun initClickListener(){
        binding.loginBtn.setOnClickListener {
          LoginActivity.navigate(this@WelcomeActivity)
        }
        binding.registerBtn.setOnClickListener {
            RegisterActivity.navigate(this@WelcomeActivity)
        }
    }

    companion object{
        fun navigate(activity: Activity){
            val intent = Intent(activity, WelcomeActivity::class.java)
            activity.startActivity(intent)
        }
    }
}