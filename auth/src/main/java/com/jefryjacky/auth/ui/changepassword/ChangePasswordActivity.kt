package com.jefryjacky.auth.ui.changepassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.authentication.design.setVisibility
import com.jefryjacky.auth.R
import com.jefryjacky.auth.databinding.ActivityChangePasswordBinding
import com.jefryjacky.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity:BaseActivity<ActivityChangePasswordBinding>() {

    private val viewModel:ChangePasswordViewModel by viewModels{viewModelFactory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserver()
    }

    private fun initView(){
        binding.submitButton.setOnClickListener {
            val currentPassword = binding.currentPassword.text.toString()
            val newPassword = binding.password.text.toString()
            val newpasswordConfirmation = binding.reinputPassword.text.toString()
            viewModel.submit(currentPassword, newPassword, newpasswordConfirmation)
        }

        binding.currentPassword.addTextChangedListener {
            binding.currentPassword.error = ""
        }
        binding.password.addTextChangedListener {
            binding.inputLayoutPassword.error= ""
        }
        binding.reinputPassword.addTextChangedListener {
            binding.reinputPassword.error = ""
        }
    }

    private fun initObserver(){
        viewModel.loadingEvent.observe(this,{
            binding.loading.loadingRoot.setVisibility(it>0)
        })
        viewModel.successEvent.observe(this, {
            it.contentIfNotHaveBeenHandle?.let {
                Snackbar.make(binding.root, getString(R.string.message_change_password_success), Snackbar.LENGTH_SHORT).show()
                finish()
            }
        })
        viewModel.currentPasswordErrorEvent.observe(this,{
            it.contentIfNotHaveBeenHandle?.let {
                binding.inputLayouCurrentPassword.error = it
            }
        })
        viewModel.newPasswordErrorEvent.observe(this,{
            it.contentIfNotHaveBeenHandle?.let {
                binding.inputLayoutPassword.error = it
            }
        })
        viewModel.newPasswordConfirmationErrorEvent.observe(this,{
            it.contentIfNotHaveBeenHandle?.let {
                binding.inputLayoutReinputPassword.error = it
            }
        })
    }

    override fun inflate(): ActivityChangePasswordBinding {
        return ActivityChangePasswordBinding.inflate(layoutInflater)
    }

    companion object{
        fun navigate(activity:Activity){
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            activity.startActivity(intent)
        }
    }
}