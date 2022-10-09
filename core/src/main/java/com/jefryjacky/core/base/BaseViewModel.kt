package com.jefryjacky.core.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jefryjacky.core.domain.usecase.BaseUseCase
import kotlin.math.max

abstract class BaseViewModel:ViewModel() {
    private val usecases = mutableListOf<BaseUseCase>()
    val loadingEvent = MutableLiveData<Int>()
    val unauthorizedEvent = MutableLiveData<Event<Any>>()
    val unknownErrorEvent = MutableLiveData<Event<String>>()

    protected fun setLoading(loading:Boolean){
        if(loading){
            val count = loadingEvent.value?:0
            loadingEvent.value = count.plus(1)
        } else{
            val count = loadingEvent.value?:1
            loadingEvent.value = max(count.minus(1), 0)
        }
    }

    protected fun addUseCase(useCase: BaseUseCase){
        useCase.callBack = {
            when(it){
                is BaseUseCase.CommonError.UNAUTHORIZED_ERROR -> {
                    unauthorizedEvent.value = Event(Any())
                    setLoading(false)
                }
                is BaseUseCase.CommonError.UNKNOWN_ERROR -> {
                    unknownErrorEvent.value = Event(it.message)
                    setLoading(false)
                }
            }
        }
        usecases.add(useCase)
    }

    override fun onCleared() {
        super.onCleared()
        usecases.forEach { it.clear() }
    }
}