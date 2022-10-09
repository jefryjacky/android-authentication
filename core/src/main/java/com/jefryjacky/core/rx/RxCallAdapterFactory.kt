package com.jefryjacky.core.rx

import com.google.gson.Gson
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.core.domain.exception.InvetisApiException
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type
import java.net.SocketTimeoutException

class RxCallAdapterFactory private constructor(schedulers: Schedulers, val gson: Gson): CallAdapter.Factory()  {
    private val original by lazy{
        RxJava2CallAdapterFactory.createWithScheduler(schedulers.netWorkThread())
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        val wrapped = original.get(returnType, annotations, retrofit) as CallAdapter<out Any, *>
        return RxCallAdapterWrapper(retrofit, wrapped)
    }

    private inner class RxCallAdapterWrapper<R>(
        val retrofit: Retrofit,
        val wrappedCallAdapter: CallAdapter<R, *>): CallAdapter<R, Any?>{

        override fun responseType(): Type = wrappedCallAdapter.responseType()

        override fun adapt(call: Call<R>): Any? {
            return when(val adapt: Any = wrappedCallAdapter.adapt(call)){
                is Single<*> ->{
                    adapt
                        .retry(NUMBER_OF_RETRY){
                          isRequiredRetry(it)
                        }.onErrorResumeNext { throwable -> Single.error(akseleranApiException(throwable)) }
                }
                is Completable -> {
                    adapt.retry(NUMBER_OF_RETRY){
                     isRequiredRetry(it)
                    }.onErrorResumeNext { throwable -> Completable.error(akseleranApiException(throwable)) }
                }
                else -> null
            }
        }


        private fun isRequiredRetry(throwable:Throwable):Boolean{
            if(throwable is HttpException){
                return throwable.code() != 401 && throwable.code() != 403
            } else if(throwable is SocketTimeoutException){
                return true
            }
            return false
        }

        private fun akseleranApiException(throwable: Throwable):Throwable{
            if(throwable is HttpException){
                throwable.response()?.let {  response ->
                    response.errorBody()?.let { errorBody->
                        var response: ErrorResponse? = null
                        try {
                            val converter = retrofit.responseBodyConverter<ErrorResponse>(
                                ErrorResponse::class.java, arrayOfNulls(0))
                            response = converter.convert(errorBody)
                        } catch (e:Exception){}

                        return InvetisApiException(
                            throwable.code(),
                            response?.error?:"",
                            response?.message?:""
                        )
                    }
                }
            }
            return throwable
        }
    }

    companion object{
        private const val NUMBER_OF_RETRY = 3L
        fun create(schedulers: Schedulers, gson: Gson): CallAdapter.Factory{
            return RxCallAdapterFactory(schedulers, gson)
        }
    }
}
