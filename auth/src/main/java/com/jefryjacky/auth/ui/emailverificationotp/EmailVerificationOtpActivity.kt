package com.jefryjacky.auth.ui.emailverificationotp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import com.jefryjacky.auth.ui.theme.AuthenticationTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jefryjacky.auth.ui.emailverification.EmailVerificationRoute
import com.jefryjacky.core.ext.ObserveAsEvent
import com.jefryjacky.core.ext.hideKeyBoard
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EmailVerificationOtpActivity : ComponentActivity() {

    @Inject lateinit var emailVerificationRoute:EmailVerificationRoute
    private val viewModel by viewModels<EmailVerificationOtpViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra(KEY_EMAIL)?:""
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
                emailVerificationRoute.next(this)
            }
            viewModel.hideKeyboardEvent.ObserveAsEvent {
                hideKeyBoard()
            }
            AuthenticationTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                val loading by viewModel.loadingState.collectAsStateWithLifecycle(false)
                EmailVerificationOtpContent(
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
        fun navigate(activity: Activity, email: String){
            val intent = Intent(activity, EmailVerificationOtpActivity::class.java)
            intent.putExtra(KEY_EMAIL, email)
            activity.startActivity(intent)
        }
    }
}