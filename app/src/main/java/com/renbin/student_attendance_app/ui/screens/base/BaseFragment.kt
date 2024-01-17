package com.renbin.student_attendance_app.ui.screens.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    protected lateinit var navController: NavController
    protected lateinit var binding: T
    protected abstract val viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.onCreate()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        onFragmentResult()
        setupUIComponents(view)
        setupViewModelObserver()
    }


    protected open fun onFragmentResult() {}

    protected open fun setupUIComponents(view: View) {}

    protected open fun setupViewModelObserver() {
        lifecycleScope.launch {
            viewModel.error.collect {
                showSnackBar(it, true)
            }
        }

        lifecycleScope.launch {
            viewModel.success.collect {
                showSnackBar(it)
            }
        }
    }

    private fun showSnackBar(msg: String, isError: Boolean = false) {
        val snackBar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
        if (isError) {
            snackBar.setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.error
                )
            )
        } else {
            snackBar.setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.success
                )
            )
        }
        snackBar.show()
    }

    fun logMsg(msg: String, tag: String = "debugging"){
        Log.d(tag, msg)
    }
}