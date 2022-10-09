package com.jefryjacky.auth.di

import androidx.lifecycle.ViewModel
import com.jefryjacky.auth.ui.changepassword.ChangePasswordViewModel
import com.jefryjacky.auth.ui.emailverification.EmailVerificationViewModel
import com.jefryjacky.auth.ui.forgotpassword.ForgotPasswordViewModel
import com.jefryjacky.auth.ui.login.LoginViewModel
import com.jefryjacky.auth.ui.register.RegisterViewModel
import com.jefryjacky.auth.ui.resetpassword.ResetPasswordViewModel
import com.jefryjacky.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EmailVerificationViewModel::class)
    abstract fun bindEmailVerification(emailVerificationViewModel: EmailVerificationViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    abstract fun bindForgotPasswordViewModel(forgotPasswordViewModel: ForgotPasswordViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    abstract fun bindResetPasswordViewModel(resetPasswordViewModel: ResetPasswordViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    abstract fun bindChangePasswordViewModel(changePasswordViewModel: ChangePasswordViewModel):ViewModel
}