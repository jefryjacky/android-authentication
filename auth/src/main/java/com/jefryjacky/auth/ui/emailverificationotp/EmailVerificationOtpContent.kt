package com.jefryjacky.auth.ui.emailverificationotp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jefryjacky.auth.ui.theme.AuthenticationTheme


@Composable
fun EmailVerificationOtpContent(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun EmailVerificationOtpContent() {
    AuthenticationTheme {
        EmailVerificationOtpContent("Android")
    }
}