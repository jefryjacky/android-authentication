package com.jefryjacky.auth.ui.verifyforgotpasswordbyotp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jefryjacky.auth.AuthConfig
import com.jefryjacky.auth.ui.emailverification.EmailVerificationRoute
import com.jefryjacky.auth.ui.login.LoginActivity
import com.jefryjacky.core.ext.ObserveAsEvent
import com.jefryjacky.core.ext.hideKeyBoard
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VerifyForgotPasswordByOtpActivity : ComponentActivity() {

    @Inject lateinit var emailVerificationRoute:EmailVerificationRoute
    private val viewModel by viewModels<VerifyForgotPasswordByOtpViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra(KEY_EMAIL)?:""
        viewModel.newPassword = intent.getStringExtra(KEY_PASSWORD)?:""
        viewModel.setEmail(email = email)
        viewModel.requestOtp()
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            viewModel.errorFlow.ObserveAsEvent{
                scope.launch {
                    snackbarHostState.showSnackbar(it)
                    Log.d("error", it)

                }
            }
            viewModel.successEvent.ObserveAsEvent {
                finish()
                LoginActivity.navigateTop(this)
            }
            viewModel.hideKeyboardEvent.ObserveAsEvent {
                hideKeyBoard()
            }
            AuthConfig.Theme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                val loading by viewModel.loadingState.collectAsStateWithLifecycle(false)
                VerifyForgotPasswordByOtpContent(
                    snackbarHostState,
                    loading,
                    state = state
                ){
                    viewModel.handleEvent(it)
                }
            }
        }
    }

    companion object{
        private const val KEY_EMAIL = "KEY EMAIL"
        private const val KEY_PASSWORD = "KEY PASSWORD"
        fun navigate(activity: Activity, email: String, password: String){
            val intent = Intent(activity, VerifyForgotPasswordByOtpActivity::class.java)
            intent.putExtra(KEY_EMAIL, email)
            intent.putExtra(KEY_PASSWORD, password)
            activity.startActivity(intent)
        }
    }
}