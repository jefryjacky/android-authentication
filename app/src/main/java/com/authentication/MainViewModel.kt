package com.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jefryjacky.auth.domain.repository.database.UserDatabase
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.base.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userDatabase: UserDatabase
) : BaseViewModel() {

    val navigateToHome = MutableLiveData<Event<Any>>()
    val navigateToWelcome = MutableLiveData<Event<Any>>()

    fun checkLoginStatus() {
        viewModelScope.launch {
            val token = withContext(Dispatchers.IO) {
                userDatabase.getToken()
            }
            if (token != null && token.accessToken.isNotBlank()) {
                navigateToHome.value = Event(Any())
            } else {
                navigateToWelcome.value = Event(Any())
            }
        }
    }
}
