package com.jefryjacky.auth.ui.forgotpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.authentication.design.setVisibility
import com.jefryjacky.auth.R
import com.jefryjacky.auth.databinding.ActivityForgotPasswordBinding
import com.jefryjacky.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity:BaseActivity<ActivityForgotPasswordBinding>() {

    private val viewModel:ForgotPasswordViewModel by viewModels { viewModelFactory }

    override fun inflate(): ActivityForgotPasswordBinding {
        return ActivityForgotPasswordBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserver()
    }

    private fun initView(){
        binding.sendBtn.setOnClickListener {
            val email = binding.email.text.toString()
            viewModel.forgotPassword(email)
            hideKeyBoard()
        }
    }

    private fun initObserver() {
        initObserver(viewModel)
        viewModel.loadingEvent.observe(this) {
            binding.loadingLayout.loadingRoot.setVisibility(it > 0)
        }
        viewModel.successRequestResetEvent.observe(this) {
            it.contentIfNotHaveBeenHandle?.let {
                Snackbar.make(
                    binding.root,
                    getString(R.string.message_reset_link_sent),
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(
                        R.string.dismiss
                    ) { }.show()
            }
        }
        viewModel.emailErrorEvent.observe(this) {
            it.contentIfNotHaveBeenHandle?.let {
                binding.inputLayoutEmail.error = it
            }
        }
    }

    companion object{
        fun navigate(activity:Activity){
            val intent = Intent(activity, ForgotPasswordActivity::class.java)
            activity.startActivity(intent)
        }
    }
}