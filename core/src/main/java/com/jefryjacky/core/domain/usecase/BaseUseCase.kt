package com.jefryjacky.core.domain.usecase

import com.jefryjacky.core.domain.exception.InvetisApiException
import io.reactivex.disposables.CompositeDisposable


abstract class BaseUseCase {
    protected val disposables by lazy {
        CompositeDisposable()
    }

    lateinit var callBack:((CommonError)->Unit)
    protected var executing = false

    protected fun checkError(throwable: Throwable){
        if(throwable is InvetisApiException){
            if(throwable.code == 401){
                callBack.invoke(CommonError.UNAUTHORIZED_ERROR)
               return
            }
        }
        callBack.invoke(CommonError.UNKNOWN_ERROR(throwable.message ?: ""))
    }

    interface BaseCallback{
        fun loading(loading:Boolean)
        fun errors(errors:List<Error>)
    }

    interface Error

    sealed class CommonError:Error{
        object UNAUTHORIZED_ERROR: CommonError()
        data class UNKNOWN_ERROR(val message:String): CommonError()
    }

    sealed interface Result {
        data class Success<T>(val data:T): Result
        data class Loading(val isLoading: Boolean): Result
        data class Error(val errors: List<BaseUseCase.Error>): Result
    }

    fun clear(){
        disposables.clear()
    }
}

