package com.jefryjacky.auth.di

import android.content.Context
import com.jefryjacky.auth.domain.message.Message
import com.jefryjacky.auth.message.MessageImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    @Singleton
    fun provideMessage(context:Context):Message{
        return MessageImpl(context)
    }
}