package com.jefryjacky.auth.ui.resetpassword

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.authentication.design.setVisibility
import com.jefryjacky.auth.databinding.ActivityResetPasswordBinding
import com.jefryjacky.auth.ui.login.LoginActivity
import com.jefryjacky.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity:BaseActivity<ActivityResetPasswordBinding>() {

    private val viewModel:ResetPasswordViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserver()
    }

    private fun initView(){
        binding.submitButton.setOnClickListener {
            hideKeyBoard()
            val token = intent.data?.getQueryParameter("reset_token")?:""
            val password = binding.password.text.toString()
            val passwordConfirmation = binding.reinputPassword.text.toString()
            viewModel.submit(password = password, confirmationPassword = passwordConfirmation, token = token)
        }
        binding.password.addTextChangedListener {
            binding.inputLayoutPassword.error = ""
        }
        binding.reinputPassword.addTextChangedListener {
            binding.inputLayoutReinputPassword.error = ""
        }
    }

    private fun initObserver(){
        initObserver(viewModel)
        viewModel.loadingEvent.observe(this){
            binding.loading.loadingRoot.setVisibility(it>0)
        }
        viewModel.passwordErrorEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let {
                binding.inputLayoutPassword.error = it
            }
        }
        viewModel.passwordConfirmationErrorEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let {
                binding.inputLayoutReinputPassword.error = it
            }
        }
        viewModel.successEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let {
                LoginActivity.navigate(this)
                finish()
            }
        }
        viewModel.errorEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun inflate(): ActivityResetPasswordBinding {
        return ActivityResetPasswordBinding.inflate(layoutInflater)
    }
}