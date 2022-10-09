package com.jefryjacky.auth.di

import android.content.Context
import com.jefryjacky.auth.database.user.UserDao
import com.jefryjacky.auth.database.AppRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    fun provideUserDao(applicationContext: Context): UserDao {
        return AppRoomDatabase.getInstance(applicationContext).userDao()
    }
}