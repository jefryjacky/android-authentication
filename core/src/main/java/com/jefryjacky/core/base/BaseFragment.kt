package com.jefryjacky.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.jefryjacky.core.R
import com.jefryjacky.core.di.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment<T: ViewBinding>: Fragment() {
    private var _binding: T? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container)
        return binding.root
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

    protected open fun unauthorized(){
        // no implementation
    }

    protected open fun unknownError(message:String){
        Snackbar.make(binding.root, getString(R.string.opps_something_wrong), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun inflate(inflater: LayoutInflater,
                         container: ViewGroup?):T

    abstract fun inject()
}