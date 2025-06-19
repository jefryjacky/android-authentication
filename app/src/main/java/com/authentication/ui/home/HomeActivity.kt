package com.authentication.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.authentication.databinding.ActivityHomeBinding
import com.jefryjacky.auth.ui.changepassword.ChangePasswordActivity
import com.jefryjacky.auth.ui.login.LoginActivity
import com.jefryjacky.auth.ui.updateuser.UpdateUserActivity
import com.jefryjacky.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity:BaseActivity<ActivityHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>{viewModelFactory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserver()
    }

    private fun initView(){
        binding.button.setOnClickListener {
            ChangePasswordActivity.navigate(this)
        }
        binding.buttonUpdateProfile.setOnClickListener {
            UpdateUserActivity.navigate(this, true)
        }
        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun initObserver(){
        viewModel.navigateToLogin.observe(this) {
            it.contentIfNotHaveBeenHandle?.let {
                LoginActivity.navigate(this)
            }
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