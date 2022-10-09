package com.jefryjacky.core.di

import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.scheduler.SchedulersImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    @Provides
    @Singleton
    fun provideScheduler():Schedulers{
        return SchedulersImpl()
    }
}