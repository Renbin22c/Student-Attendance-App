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
    // Navigation controller for fragment navigation
    protected lateinit var navController: NavController
    // ViewBinding instance for the fragment
    protected lateinit var binding: T
    // ViewModel instance for the fragment, must be implemented by child classes
    protected abstract val viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Launch the ViewModel's onCreate method when the fragment's lifecycle is in the RESUMED state
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.onCreate()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)

        // Hook for handling fragment result data
        onFragmentResult()

        // Set up UI components specific to the fragment
        setupUIComponents(view)

        // Observe ViewModel events
        setupViewModelObserver()
    }

    // Open hook for handling fragment result data
    protected open fun onFragmentResult() {}

    // Open method to set up UI components, to be overridden by child fragments
    protected open fun setupUIComponents(view: View) {}

    // Open method to set up ViewModel observers, to be overridden by child fragments
    protected open fun setupViewModelObserver() {
        // Observe error events and show a Snackbar with an error message
        lifecycleScope.launch {
            viewModel.error.collect {
                showSnackBar(it, true)
            }
        }

        // Observe success events and show a Snackbar with a success message
        lifecycleScope.launch {
            viewModel.success.collect {
                showSnackBar(it)
            }
        }
    }

    // Show a Snackbar with a given message, with an optional error flag to set the background color
    private fun showSnackBar(msg: String, isError: Boolean = false) {
        val snackBar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
        if (isError) {
            snackBar.setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.error
                )
            )
        }
        snackBar.show()
    }

    // Log a message with a given tag (default tag is "debugging")
    fun logMsg(msg: String, tag: String = "debugging") {
        Log.d(tag, msg)
    }
}