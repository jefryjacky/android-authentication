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
        
        // Request POST_NOTIFICATIONS permission for Android 13+ to allow Chucker notifications
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
        
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