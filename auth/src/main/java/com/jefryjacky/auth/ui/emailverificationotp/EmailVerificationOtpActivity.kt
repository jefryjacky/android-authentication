package com.jefryjacky.auth.ui.emailverificationotp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.jefryjacky.auth.ui.theme.AuthenticationTheme

class EmailVerificationOtpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthenticationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EmailVerificationOtpContent(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
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