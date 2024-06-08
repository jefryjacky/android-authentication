package com.jefryjacky.auth.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.authentication.design.setVisibility
import com.jefryjacky.core.base.BaseActivity
import com.jefryjacky.auth.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity: BaseActivity<ActivityRegisterBinding>() {

    private val viewModel: RegisterViewModel by viewModels {viewModelFactory}
    var router:RegisterRoute?=null

    override fun inflate(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
        initClickListener()
    }

    private fun initObserver(){
        initObserver(viewModel)
        viewModel.loadingEvent.observe(this){
            binding.loading.loadingRoot.setVisibility(it>0)
        }
        viewModel.emailErrorEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let {
                binding.inputLayoutEmail.error = it
            }
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

        viewModel.registerSuccessEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let {
                router?.next(this, it)
            }
        }
    }

    private fun initClickListener(){
        binding.registerBtn.setOnClickListener {
            hideKeyBoard()
            viewModel.register(
                binding.email.text.toString(),
                binding.password.text.toString(),
                binding.reinputPassword.text.toString()
            )
        }

        binding.email.addTextChangedListener {
            binding.inputLayoutEmail.error = ""
        }

        binding.password.addTextChangedListener {
            binding.inputLayoutPassword.error = ""
        }

        binding.reinputPassword.addTextChangedListener {
            binding.inputLayoutReinputPassword.error = ""
        }
    }

    companion object{
        fun navigate(activity: Activity){
            val intent = Intent(activity, RegisterActivity::class.java)
            activity.startActivity(intent)
        }
    }
}