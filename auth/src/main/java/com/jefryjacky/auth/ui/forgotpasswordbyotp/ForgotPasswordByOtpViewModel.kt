package com.jefryjacky.auth.ui.forgotpasswordbyotp

import androidx.lifecycle.viewModelScope
import com.jefryjacky.auth.config.Config
import com.jefryjacky.auth.domain.message.Message
import com.jefryjacky.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordByOtpViewModel @Inject constructor(
    private val message: Message
): BaseViewModel() {

    private val _state = MutableStateFlow<ForgotPasswordByOtpState>(ForgotPasswordByOtpState())
    val state = _state.asStateFlow()

    private val _success = Channel<Any>()
    val success = _success.receiveAsFlow()

    fun handleEvent(event: ForgotPasswordByOtpEvent){
        when(event){
            is ForgotPasswordByOtpEvent.TypingEmailEvent -> {
                _state.update {
                    it.copy(email = event.email)
                }
                isEmailValid()
            }
            is ForgotPasswordByOtpEvent.TypingNewPasswordEvent -> {
                _state.update {
                    it.copy(newPassword = event.newPassword)
                }
                isPasswordValid()
            }
            is ForgotPasswordByOtpEvent.TypingConfirmPasswordEvent -> {
                _state.update {
                    it.copy(confirmPassword = event.confirmPassword)
                }
                isPasswordConfirmationValid()
            }
            ForgotPasswordByOtpEvent.NextEvent -> {
                if(isEmailValid() && isPasswordValid() && isPasswordConfirmationValid()){
                    viewModelScope.launch {
                        _success.send(Any())
                    }
                }
            }
        }
    }

    private fun isEmailValid():Boolean{
        if(state.value.email.isBlank()){
            _state.update {
                it.copy(errorMail = message.messageErrorEmptyEmail())
            }
            return false
        } else if(!Pattern.matches(Config.rfc5352StandardEmailPattern, state.value.email)){
            _state.update {
                it.copy(errorMail = message.messageErrorInvalidEmail())
            }
            return false
        }
        _state.update {
            it.copy(errorMail = "")
        }
        return true
    }

    private fun isPasswordValid(): Boolean{
        if(state.value.newPassword.isBlank()){
            _state.update {
                it.copy(errorNewPassword = message.messageErrorPasswordEmpty())
            }
            return false
        } else if(state.value.newPassword.length < Config.PASSWORD_MIN_LENGTH){
            _state.update {
                it.copy(errorNewPassword = message.messageErrorPasswordMinLength(Config.PASSWORD_MIN_LENGTH))
            }
            return false
        } else if(!Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE).matcher(state.value.newPassword).find()){
            _state.update {
                it.copy(errorNewPassword = message.messageErrorPasswordNoSpecialChars())
            }
            return false
        } else if(!state.value.newPassword.any { it.isUpperCase() }){
            _state.update {
                it.copy(errorNewPassword = message.messageErrorPasswordNoCapitalChar())
            }
            return false
        }
        _state.update {
            it.copy(errorNewPassword = "")
        }
        return true
    }

    fun isPasswordConfirmationValid(): Boolean{
        if(state.value.newPassword != state.value.confirmPassword){
            _state.update {
                it.copy(errorConfirmPassword = message.messagePasswordIsNotSameWithAbove())
            }
            return false
        }
        _state.update {
            it.copy(errorConfirmPassword = "")
        }
        return true
    }
}