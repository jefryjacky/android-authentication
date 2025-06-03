package com.jefryjacky.core.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.jefryjacky.core.domain.usecase.BaseUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.math.max

abstract class BaseViewModel:ViewModel() {
    private val usecases = mutableListOf<BaseUseCase>()
    val loadingEvent = MutableLiveData<Int>()

    val unauthorizedEvent = MutableLiveData<Event<Any>>()
    val unknownErrorEvent = MutableLiveData<Event<String>>()

    protected val _errorFlow = Channel<String>()
    val errorFlow = _errorFlow.receiveAsFlow()

    val loadingState = loadingEvent.asFlow().map {
        it>0
    }.distinctUntilChanged().debounce(500)

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
                    viewModelScope.launch {
                        _errorFlow.send(it.message)
                    }
                    setLoading(false)
                }
            }
        }
        usecases.add(useCase)
    }

    protected suspend fun handleError(e: Exception){
        if(e is HttpException){
            val json = e.response()?.errorBody()?.string()
            if(!json.isNullOrEmpty()) {
                val jsonObject = org.json.JSONObject(json)
                val message = jsonObject.getString("message")
                _errorFlow.send(message)
                return
            }
        }
        _errorFlow.send(e.message.toString())
    }

    override fun onCleared() {
        super.onCleared()
        usecases.forEach { it.clear() }
    }
}