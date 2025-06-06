package com.jefryjacky.auth.ui.forgotpasswordbyotp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.jefryjacky.auth.AuthConfig
import com.jefryjacky.auth.ui.verifyforgotpasswordbyotp.VerifyForgotPasswordByOtpActivity
import com.jefryjacky.core.ext.ObserveAsEvent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordByOtpActivity: ComponentActivity() {

    private val viewModel by viewModels<ForgotPasswordByOtpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthConfig.Theme {
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                val loading by viewModel.loadingState.collectAsStateWithLifecycle(false)
                val state by viewModel.state.collectAsStateWithLifecycle()

                viewModel.errorFlow.ObserveAsEvent{
                    scope.launch {
                        snackbarHostState.showSnackbar(it)
                    }
                }

                viewModel.success.ObserveAsEvent{
                    VerifyForgotPasswordByOtpActivity.navigate(this, state.email, state.newPassword)
                }

                ForgotPasswordByOtpContent(snackbarHostState,
                    loading, state) {
                    viewModel.handleEvent(it)
                }
            }
        }
    }

    companion object{
        fun navigate(activity: Activity){
            val intent = Intent(activity, ForgotPasswordByOtpActivity::class.java)
            activity.startActivity(intent)
        }
    }
}