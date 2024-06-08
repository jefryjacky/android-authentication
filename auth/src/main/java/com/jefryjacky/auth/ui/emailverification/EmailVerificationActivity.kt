package com.jefryjacky.auth.ui.emailverification

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.jefryjacky.auth.R
import com.jefryjacky.core.base.BaseActivity
import com.jefryjacky.auth.databinding.ActivityEmailVerificationBinding
import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.ui.login.LoginActivity
import com.jefryjacky.auth.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EmailVerificationActivity: BaseActivity<ActivityEmailVerificationBinding>() {

    private val viewModel: EmailVerificationViewModel by viewModels { viewModelFactory }
    var router:EmailVerificationRoute? = null

    val countDownTimer = object:CountDownTimer(300000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            var seconds = (millisUntilFinished / 1000).toInt()
            val minutes = seconds / 60
            seconds %= 60
            val remainingTime = String.format("%d:%02d", minutes,seconds)
            binding.resendBtn.text = getString(R.string.resend_after, remainingTime)
        }

        override fun onFinish() {
            binding.resendBtn.text = getString(R.string.button_resend_verication_email)
            binding.resendBtn.isEnabled = true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserver()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val appLinkAction = intent.action
        val token = intent.data?.getQueryParameter("token")

        if(Intent.ACTION_VIEW == appLinkAction){
            token?.let {
                viewModel.verifyEmail(token)
            }
        }
    }

    private fun initView(){
        countDownTimer.start()
        val email = intent.extras?.getString(KEY_EMAIL)!!

        binding.resendBtn.setOnClickListener {
            viewModel.requestEmailVerification(email)
            countDownTimer.start()
            binding.resendBtn.isEnabled = false
        }
        binding.resendBtn.isEnabled = false

        binding.texttviewDescription.text =
            getString(R.string.email_verification_description, email)

        viewModel.requestEmailVerification(email)
    }

    private fun initObserver(){
        viewModel.successVerifyEmailEvent.observe(this,{
            it.contentIfNotHaveBeenHandle?.let {
                router?.next(this)
            }
        })

        viewModel.failedVerifyEmailEvent.observe(this,{
            it.contentIfNotHaveBeenHandle?.let {
                Snackbar.make(binding.root, getString(R.string.message_failed_verify_email), Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.dismiss
                    ) {  }.show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
        countDownTimer.onFinish()
    }

    override fun inflate(): ActivityEmailVerificationBinding {
        return ActivityEmailVerificationBinding.inflate(layoutInflater)
    }

    companion object{
        private const val KEY_EMAIL = "KEY EMAIL"
        fun navigate(activity: Activity, email:String){
            val intent = Intent(activity, EmailVerificationActivity::class.java)
            intent.putExtra(KEY_EMAIL, email)
            activity.startActivity(intent)
        }
    }
}