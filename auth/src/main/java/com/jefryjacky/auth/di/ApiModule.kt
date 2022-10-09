package com.jefryjacky.auth.di

import com.jefryjacky.auth.api.user.UserApiImpl
import com.jefryjacky.auth.domain.repository.api.UserApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    @Binds
    abstract fun bindUserApi(userApi: UserApiImpl):UserApi
}