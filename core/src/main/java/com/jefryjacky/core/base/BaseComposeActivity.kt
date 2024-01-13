package com.jefryjacky.core.base

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.jefryjacky.core.R
import com.jefryjacky.core.di.ViewModelFactory
import javax.inject.Inject

abstract class BaseComposeActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun initObserver(viewModel:BaseViewModel){
        viewModel.unauthorizedEvent.observe(this) {
            it.contentIfNotHaveBeenHandle?.let {
                unauthorized()
            }
        }
        viewModel.unknownErrorEvent.observe(this) {
            it.contentIfNotHaveBeenHandle?.let {
                unknownError(it)
            }
        }
    }

    protected fun hideKeyBoard(){
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    protected open fun unauthorized(){
        // no implementation
    }

    protected open fun unknownError(message:String){
        Toast.makeText(this, getString(R.string.opps_something_wrong), Toast.LENGTH_SHORT).show()
    }
}