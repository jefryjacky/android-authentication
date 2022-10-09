package com.jefryjacky.auth.di

import com.jefryjacky.auth.database.user.UserDatabaseImpl
import com.jefryjacky.auth.domain.repository.database.UserDatabase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
    @Binds
    @Singleton
    abstract fun bindUserDatabase(userDatabaseImpl: UserDatabaseImpl): UserDatabase
}