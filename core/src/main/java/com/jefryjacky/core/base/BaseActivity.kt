package com.jefryjacky.core.base

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.jefryjacky.core.R
import com.jefryjacky.core.di.ViewModelFactory
import javax.inject.Inject

abstract class BaseActivity<T: ViewBinding>: AppCompatActivity() {
    lateinit var binding:T

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate()
        setContentView(binding.root)
    }

    protected fun initObserver(viewModel:BaseViewModel){
        viewModel.unauthorizedEvent.observe(this, {
            it.contentIfNotHaveBeenHandle?.let {
                unauthorized()
            }
        })
        viewModel.unknownErrorEvent.observe(this,{
            it.contentIfNotHaveBeenHandle?.let {
                unknownError(it)
            }
        })
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
        Snackbar.make(binding.root, getString(R.string.opps_something_wrong), Snackbar.LENGTH_SHORT).show()
    }

    abstract fun inflate():T
}