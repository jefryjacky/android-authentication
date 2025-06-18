package com.jefryjacky.auth.ui.updateuser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefryjacky.auth.AuthConfig
import com.jefryjacky.auth.R
import com.jefryjacky.auth.ui.theme.AuthenticationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserContent(
    snackbarHostState: SnackbarHostState,
    loading: Boolean,
    state: UpdateUserState,
    event: (UpdateUserEvent)-> Unit
) {
    Scaffold (
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = stringResource(R.string.profile),
                        style = AuthConfig.Typography.titleLarge
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ){
        Box(modifier = Modifier.padding(it)
            .padding(16.dp)){
            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        if(state.displayNameError.isNotBlank()) {
                            Text(text = state.displayNameError)
                        }
                    },
                    label = {
                        Text(text = stringResource(R.string.display_name))
                    },
                    value = state.displayName,
                    onValueChange = {
                        event(UpdateUserEvent.TypingDisplayNameEvent(it))
                    }
                )
                Spacer(Modifier.weight(1f))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    onClick = {
                    event(UpdateUserEvent.Submit)
                }) {
                    Text(text = stringResource(R.string.button_submit))
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


@Preview(showBackground = true)
@Composable
fun UpdateUserContentPreview() {
    val snackbarHostState = SnackbarHostState()
    val state = UpdateUserState()
    AuthenticationTheme {
        UpdateUserContent(snackbarHostState, state = state, loading = true){

        }
    }
}