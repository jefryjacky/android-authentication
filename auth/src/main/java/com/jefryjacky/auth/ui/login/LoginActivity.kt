package com.jefryjacky.auth.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.authentication.design.setVisibility
import com.jefryjacky.auth.AuthConfig
import com.jefryjacky.core.base.BaseActivity
import com.jefryjacky.auth.R
import com.jefryjacky.auth.config.Config
import com.jefryjacky.auth.databinding.ActivityLoginBinding
import com.jefryjacky.auth.ui.forgotpassword.ForgotPasswordActivity
import com.jefryjacky.auth.ui.forgotpasswordbyotp.ForgotPasswordByOtpActivity
import com.jefryjacky.auth.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: BaseActivity<ActivityLoginBinding>() {

    private val viewmodel: LoginViewModel by viewModels { viewModelFactory }

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!

            viewmodel.loginGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.e(Config.TAG_LOG,"Google sign in failed ${e.message}")
            Snackbar.make(binding.root, getString(R.string.message_unathorized_google), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.dismiss
                ) {  }.show()
        }
    }

    override fun inflate(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserver()
    }

    private fun initView(){
        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            viewmodel.login(email, password)
            hideKeyBoard()
        }
        binding.googleSignInButton.setOnClickListener {
            Log.d(Config.TAG_LOG,"Google sign in with request Id Token ${AuthConfig.GOOGLE_AUTH_ID}")

            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(AuthConfig.GOOGLE_AUTH_ID)
                .requestEmail()
                .build()
            hideKeyBoard()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }
        binding.forgotPassword.setOnClickListener {
            if(AuthConfig.FORGOT_PASSWORD_BY_OTP){
                ForgotPasswordByOtpActivity.navigate(this)
            } else if(AuthConfig.FORGOT_PASSWORD_BY_LINK){
                ForgotPasswordActivity.navigate(this)
            }
        }

        binding.email.addTextChangedListener {
            binding.inputLayoutEmail.error = ""
        }

        binding.password.addTextChangedListener {
            binding.inputLayoutPassword.error = ""
        }

        binding.signup.setOnClickListener {
            RegisterActivity.navigate(this)
        }
    }

    private fun initObserver(){
        initObserver(viewmodel)
        viewmodel.loadingEvent.observe(this){
            binding.loadingLayout.loadingRoot.setVisibility(it>0)
        }

        viewmodel.loginSuccessEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let { user->
                viewmodel.navigateNext(this, user)
            }
        }

        viewmodel.emailErrorEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let {
                binding.inputLayoutEmail.error = it
            }
        }

        viewmodel.passwordErrorEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let {
                binding.inputLayoutPassword.error = it
            }
        }

        viewmodel.loginGoogleFailedEvent.observe(this){
            it.contentIfNotHaveBeenHandle?.let {
                Snackbar.make(binding.root, getString(R.string.message_unathorized_google), Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.dismiss
                    ) {  }.show()
            }
        }
    }

    override fun unauthorized() {
        super.unauthorized()
        Snackbar.make(binding.root, getString(R.string.message_unathorized), Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.dismiss
            ) {  }.show()
    }

    companion object{
        fun navigate(activity: Activity){
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }

        fun navigateTop(activity: Activity){
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(intent)
        }
    }
}