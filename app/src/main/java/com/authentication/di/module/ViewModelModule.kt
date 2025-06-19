package com.authentication.di.module

import androidx.lifecycle.ViewModel
import com.authentication.ui.home.HomeViewModel
import com.jefryjacky.auth.ui.login.LoginViewModel
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
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(loginViewModel: HomeViewModel): ViewModel
}