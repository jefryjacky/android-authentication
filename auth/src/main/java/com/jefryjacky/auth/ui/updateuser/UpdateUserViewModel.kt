package com.jefryjacky.auth.ui.updateuser

import androidx.lifecycle.viewModelScope
import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.domain.usecase.GetUserUseCase
import com.jefryjacky.auth.domain.usecase.UpdateUserUseCase
import com.jefryjacky.core.base.BaseViewModel
import com.jefryjacky.core.domain.usecase.BaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateUserViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase
): BaseViewModel() {

    private val _state = MutableStateFlow(UpdateUserState())
    val state = _state.asStateFlow()

    private val _successEvent = Channel<Any>()
    val successEvent = _successEvent.receiveAsFlow()

    init {
        getUserUseCase.execute(callback = object : GetUserUseCase.Callback{
            override fun success(user: User) {
                _state.update {
                    it.copy(displayName = user.displayName)
                }
            }

            override fun loading(loading: Boolean) {
                setLoading(loading)
            }

            override fun errors(errors: List<BaseUseCase.Error>) {

            }
        })
    }

    fun handleEvent(event: UpdateUserEvent){
        when(event){
            is UpdateUserEvent.TypingDisplayNameEvent -> {
                _state.update {
                    it.copy(displayName = event.displayName, displayNameError = "")
                }
            }
            UpdateUserEvent.Submit -> {
                updateUserUseCase.execute(UpdateUserUseCase.Input(displayName = state.value.displayName),
                    callback = object : UpdateUserUseCase.Callback{
                        override fun success() {
                            viewModelScope.launch {
                                _successEvent.send(Any())
                            }
                        }

                        override fun loading(loading: Boolean) {
                            setLoading(loading)
                        }

                        override fun errors(errors: List<BaseUseCase.Error>) {
                            errors.forEach { error ->
                                when (error) {
                                    is UpdateUserUseCase.UpdateUserUseCaseError.DisplayNameError -> {
                                        _state.update {
                                            it.copy(displayNameError = error.message)
                                        }
                                    }
                                }
                            }
                        }
                    })
            }
        }
    }
}