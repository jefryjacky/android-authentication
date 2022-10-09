package com.authentication.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.authentication.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module(includes = [RetrofitServiceModule::class])
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideOkHttpClient(context: Context): OkHttpClient {

        val trusAllCert = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trusAllCert, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trusAllCert[0] as X509TrustManager)
        builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })

        if(BuildConfig.DEBUG){
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        if(BuildConfig.DEBUG || BuildConfig.FLAVOR == "staging"){
            builder.addInterceptor(ChuckerInterceptor.Builder(context).build())
        }

        builder.addInterceptor {
            val builder = it.request().newBuilder()
            builder.addHeader("API-KEY", "")
            it.proceed(builder.build())
        }.readTimeout(25, TimeUnit.SECONDS)
        return builder.build()
    }
}