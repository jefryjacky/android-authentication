package com.authentication.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jefryjacky.auth.domain.usecase.LogOutUseCase
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.base.Event
import com.jefryjacky.core.domain.usecase.BaseUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase
): BaseViewModel(){

    val navigateToLogin = MutableLiveData<Event<Any>>()

    fun logout(){

        viewModelScope.launch {
            logOutUseCase.execute().collect {
                when(it){
                    is BaseUseCase.Result.Loading -> setLoading(it.isLoading)
                    is BaseUseCase.Result.Success<*> -> {
                        navigateToLogin.value = Event(Event(Any()))
                    }
                    is BaseUseCase.Result.Error -> {

                    }
                }
            }
        }
//       logOutUseCase.execute(object : LogOutUseCase.Callback{
//           override fun success() {
//               navigateToLogin.value = Event(Event(Any()))
//           }
//
//           override fun loading(loading: Boolean) {
//
//           }
//
//           override fun errors(errors: List<BaseUseCase.Error>) {
//
//           }
//       })
    }
}