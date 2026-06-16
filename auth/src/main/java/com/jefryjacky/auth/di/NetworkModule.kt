package com.jefryjacky.auth.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.jefryjacky.auth.AuthConfig
import com.jefryjacky.auth.BuildConfig
import com.jefryjacky.auth.api.user.response.TokenResponse
import com.jefryjacky.auth.config.Config
import com.jefryjacky.auth.domain.repository.database.UserDatabase
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.rx.RxCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module(includes = [RetrofitServiceModule::class])
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Named(Config.RETROFIT_USER)
    fun provideOkHttpClient(context: Context, userDatabase: UserDatabase, tokenAuthenticator: TokenAuthenticator): OkHttpClient {

        val builder = OkHttpClient.Builder()

        if(true){
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        if(BuildConfig.DEBUG || AuthConfig.SHOW_API_INFO){
            builder.addInterceptor(ChuckerInterceptor.Builder(context).build())
        }

        builder.addInterceptor {
            val requestBuilder = it.request().newBuilder()
            requestBuilder.addHeader("API-KEY", AuthConfig.API_KEY)
            val token = userDatabase.getToken()
            if(!token?.accessToken.isNullOrBlank()) {
                requestBuilder.addHeader("Authorization", token!!.accessToken)
            }
            it.proceed(requestBuilder.build())
        }.readTimeout(25, TimeUnit.SECONDS)

        builder.authenticator(tokenAuthenticator)

        return builder.build()
    }

    @Provides
    @Named(Config.RETROFIT_USER)
    fun provideRetrofit(@Named(Config.RETROFIT_USER) okHttpClient: OkHttpClient, schedulers: Schedulers): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AuthConfig.API)
            .addCallAdapterFactory(RxCallAdapterFactory.create(schedulers, Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}