package com.renbin.student_attendance_app.ui.screens.forgetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.renbin.student_attendance_app.databinding.FragmentForgetPasswordBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.forgetPassword.viewModel.ForgetPasswordViewModelImpl
import com.renbin.student_attendance_app.ui.screens.login.LoginFragmentDirections
import com.renbin.student_attendance_app.ui.screens.tabContainer.StudentTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>() {
    override val viewModel: ForgetPasswordViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnForgotPasswordSubmit?.setOnClickListener { resetPassword() }
    }

    private fun validateForm(email: String): Boolean {
        return when {
            email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding?.tilEmailForgetPassword?.error = "Enter a valid email address"
                false
            }
            else -> true
        }
    }

    private fun resetPassword() {
        val email = binding?.etForgotPasswordEmail?.text.toString()
        if (validateForm(email)) {
            viewModel.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    binding?.tilEmailForgetPassword?.visibility = View.GONE
                    binding?.tvSubmitMsg?.visibility = View.VISIBLE
                    binding?.btnForgotPasswordSubmit?.visibility = View.GONE
                } else {

                    Toast.makeText(
                        requireContext(),
                        "Password reset failed. Please check your email and try again.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        binding.run {
            btnBackToLogin.setOnClickListener {
                val action = ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToLoginFragment()
                navController.navigate(action)
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }
}
