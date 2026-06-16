package com.authentication

import android.os.Bundle
import androidx.activity.viewModels
import com.authentication.databinding.ActivityMainBinding
import com.authentication.ui.home.HomeActivity
import com.jefryjacky.auth.ui.welcome.WelcomeActivity
import com.jefryjacky.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        initObserver()
        viewModel.checkLoginStatus()
    }

    private fun initObserver() {
        viewModel.navigateToHome.observe(this) { event ->
            event.contentIfNotHaveBeenHandle?.let {
                HomeActivity.navigate(this@MainActivity)
                finish()
            }
        }

        viewModel.navigateToWelcome.observe(this) { event ->
            event.contentIfNotHaveBeenHandle?.let {
                WelcomeActivity.navigate(this@MainActivity)
                finish()
            }
        }
    }

    override fun inflate(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

}