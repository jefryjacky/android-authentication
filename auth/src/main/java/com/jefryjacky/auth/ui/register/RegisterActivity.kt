package com.jefryjacky.auth.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.authentication.design.setVisibility
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.jefryjacky.auth.AuthConfig
import com.jefryjacky.auth.R
import com.jefryjacky.core.base.BaseActivity
import com.jefryjacky.auth.databinding.ActivityRegisterBinding
import com.jefryjacky.auth.ui.login.LoginRoute
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity: BaseActivity<ActivityRegisterBinding>() {

    private val viewModel: RegisterViewModel by viewModels {viewModelFactory}

    @Inject lateinit var registerRoute:RegisterRoute

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            viewModel.loginGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Snackbar.make(binding.root, getString(R.string.message_unathorized_google), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.dismiss
                ) {  }.show()
        }
    }

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
                registerRoute.next(this, it)
            }
        }

        viewModel.registerGoogleSuccessEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let {
                registerRoute.nextGoogleLogin(this)
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

        binding.googleSignInButton.setOnClickListener {
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(AuthConfig.GOOGLE_AUTH_ID)
                .requestEmail()
                .build()
            hideKeyBoard()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    companion object{
        fun navigate(activity: Activity){
            val intent = Intent(activity, RegisterActivity::class.java)
            activity.startActivity(intent)
        }
    }
}