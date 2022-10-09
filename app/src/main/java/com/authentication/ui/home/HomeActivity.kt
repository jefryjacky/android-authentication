package com.authentication.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.authentication.databinding.ActivityHomeBinding
import com.jefryjacky.auth.ui.changepassword.ChangePasswordActivity
import com.jefryjacky.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity:BaseActivity<ActivityHomeBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView(){
        binding.button.setOnClickListener {
            ChangePasswordActivity.navigate(this)
        }
    }

    override fun inflate(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    companion object{
        fun navigate(activity:Activity){
            val intent = Intent(activity, HomeActivity::class.java)
            activity.startActivity(intent)
        }
    }
}