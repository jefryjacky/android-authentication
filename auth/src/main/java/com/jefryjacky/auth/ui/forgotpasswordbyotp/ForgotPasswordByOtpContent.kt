package com.jefryjacky.auth.ui.forgotpasswordbyotp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefryjacky.auth.AuthConfig
import com.jefryjacky.auth.R
import com.jefryjacky.auth.ui.theme.AuthenticationTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordByOtpContent(
    snackbarHostState: SnackbarHostState,
    loading: Boolean,
    state: ForgotPasswordByOtpState,
    event: (ForgotPasswordByOtpEvent) -> Unit
) {  // Define a mutable state to hold the state
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = stringResource(R.string.title_forgot_password),
                        style = AuthConfig.Typography.titleLarge
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.email,
                    isError = state.errorMail.isNotEmpty(),
                    supportingText = {
                        Text(text = state.errorMail)
                    },
                    label = {
                        Text(text = stringResource(R.string.label_email))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = {
                        event(ForgotPasswordByOtpEvent.TypingEmailEvent(it))
                    }
                )
                Spacer(Modifier.height(16.dp))
                PasswordField(
                    password = state.newPassword,
                    error = state.errorNewPassword,
                    label = stringResource(R.string.label_new_password)
                ) {
                    event(ForgotPasswordByOtpEvent.TypingNewPasswordEvent(it))
                }
                Spacer(Modifier.height(16.dp))
                PasswordField(
                    password = state.confirmPassword,
                    error = state.errorConfirmPassword,
                    label = stringResource(R.string.label_new_password_confirmation)
                ) {
                    event(ForgotPasswordByOtpEvent.TypingConfirmPasswordEvent(it))
                }
                Spacer(Modifier.weight(1f))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    onClick = {
                        event(ForgotPasswordByOtpEvent.NextEvent)
                    }) {
                    Text(text = stringResource(R.string.next))
                }
            }
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = loading
            ) {
                CircularProgressIndicator(modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun PasswordField(password: String,
                  label: String,
                  error: String = "",
                  onValueChange: (String) -> Unit){
    var showPassword by remember { mutableStateOf(false) }

    // Auto hide password after 2 seconds
    LaunchedEffect(showPassword) {
        if (showPassword) {
            delay(2000)
            showPassword = false
        }
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        supportingText = {
            Text(text = error)

        },
        isError = error.isNotEmpty(),
        label = {
            Text(text = label)
        },
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(imageVector = if(showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = "Show Password")
            }
        },
        visualTransformation = if(showPassword){
            VisualTransformation.None
        }else{
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = {
            onValueChange(it)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordByOtpContentPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = ForgotPasswordByOtpState()
    AuthenticationTheme {
        ForgotPasswordByOtpContent(
            snackbarHostState,
            loading = true,
            state
        ) {

        }
    }
}