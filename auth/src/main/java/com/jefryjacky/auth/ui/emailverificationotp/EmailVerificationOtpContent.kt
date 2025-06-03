package com.jefryjacky.auth.ui.emailverificationotp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefryjacky.auth.R
import com.jefryjacky.auth.ui.component.OtpField
import com.jefryjacky.auth.ui.theme.AuthenticationTheme
import com.jefryjacky.auth.ui.theme.Typography
import com.jefryjacky.core.ext.toCountDownDisplay
import kotlinx.coroutines.delay


@Composable
fun EmailVerificationOtpContent(
    snackbarHostState: SnackbarHostState,
    loading: Boolean,
    state: EmailVerificationOtpState,
                                event: (EmailVerificationOtpEvent)-> Unit) {
    var remainingTime by remember {
        mutableStateOf(state.expire - System.currentTimeMillis())
    }

    LaunchedEffect(key1 = state.expire, block = {
        remainingTime = state.expire - System.currentTimeMillis()
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime = state.expire - System.currentTimeMillis()
        }
    })
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            Text(
                modifier = Modifier.statusBarsPadding().padding(16.dp),
                text = stringResource(R.string.email_verification),
                style = Typography.titleLarge
            )
        }
    ) {
        Box(Modifier.fillMaxSize().padding(it)
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)) {
            Column{
                Text(text = stringResource(R.string.otp_email_verification_description, state.email),
                    style = Typography.bodySmall)
                Spacer(modifier = Modifier.height(16.dp))
                OtpField(
                    value = state.otp,
                    errorMessage = state.error,
                    onValueChange = {
                        event(EmailVerificationOtpEvent.TypeOtpEvent(it))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                if(remainingTime > 0){
                    Text(text = stringResource(R.string.resend_after, remainingTime.toCountDownDisplay()),
                        style = Typography.bodySmall)
                } else {
                    Button(
                        shape = RoundedCornerShape(6.dp),
                        onClick = {
                            event(EmailVerificationOtpEvent.RequestOtpEvent)
                        }) {
                        Text(text = "resend otp")
                    }
                }
            }
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailVerificationOtpContentPreview() {
    AuthenticationTheme(darkTheme = true) {
        val snackbarHostState = remember { SnackbarHostState() }
        val state = EmailVerificationOtpState(
            otp = "32131",
            expire = System.currentTimeMillis() + 3000000,
            email = "abc@mailinator.com"
        )
        EmailVerificationOtpContent(
            snackbarHostState,
            loading = true,
            state){

        }
    }
}