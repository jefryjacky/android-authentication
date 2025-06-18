package com.jefryjacky.auth.ui.updateuser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jefryjacky.auth.ui.theme.AuthenticationTheme
import com.jefryjacky.core.ext.ObserveAsEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UpdateUserActivity: ComponentActivity() {

    private val viewModel by viewModels<UpdateUserViewModel>()
    @Inject lateinit var updateUserRoute: UpdateUserRoute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.state.collectAsStateWithLifecycle()
            val loading by viewModel.loadingState.collectAsStateWithLifecycle(false)
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            viewModel.errorFlow.ObserveAsEvent{
                scope.launch {
                    snackbarHostState.showSnackbar(it)
                }
            }
            viewModel.successEvent.ObserveAsEvent {
                val isNextRouteClose = intent.getBooleanExtra(KEY_NEXT_ROUTE_CLOSE, false)
                if(isNextRouteClose){
                    finish()
                } else {
                    updateUserRoute.next(this)
                }
            }
            AuthenticationTheme {
                UpdateUserContent(snackbarHostState, state = state.value, loading = loading, event = viewModel::handleEvent)
            }
        }
    }

    companion object{
        private const val KEY_NEXT_ROUTE_CLOSE = "KEY_NEXT_ROUTE_CLOSE"
        fun navigate(activity: Activity, nextRouteClose:Boolean = false){
            val intent = Intent(activity, UpdateUserActivity::class.java)
            intent.putExtra(KEY_NEXT_ROUTE_CLOSE, nextRouteClose)
            activity.startActivity(intent)
        }
    }
}