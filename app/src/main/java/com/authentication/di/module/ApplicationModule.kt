package com.authentication.di.module

import android.content.Context
import com.authentication.domain.message.Message
import com.authentication.message.MessageImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideContext(@ApplicationContext appContext:Context):Context{
        return appContext
    }

    @Provides
    fun provideMessage(@ApplicationContext appContext:Context): Message {
        return MessageImpl(appContext)
    }
}