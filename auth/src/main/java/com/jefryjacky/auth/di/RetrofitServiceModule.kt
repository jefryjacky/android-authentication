package com.jefryjacky.auth.di


import com.jefryjacky.auth.api.user.UserService
import com.jefryjacky.auth.config.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class RetrofitServiceModule {

    @Provides
    fun provideUserApi(@Named(Config.RETROFIT_USER)retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}