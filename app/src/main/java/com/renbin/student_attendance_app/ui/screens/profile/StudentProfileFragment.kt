package com.renbin.student_attendance_app.ui.screens.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentStudentProfileBinding
import com.renbin.student_attendance_app.ui.adapter.EditProfileAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.login.LoginFragmentDirections
import com.renbin.student_attendance_app.ui.screens.profile.viewModel.StudentProfileViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.StudentTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentProfileFragment : BaseFragment<FragmentStudentProfileBinding>() {
    override val viewModel: StudentProfileViewModelImpl by viewModels()
    private val PICK_IMAGE_REQUEST_CODE = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.cvEditProfile?.setOnClickListener {
            val bottomSheetFragment = EditProfileAdapter(this) { name, profilePicUri ->
                onProfileUpdated(name, profilePicUri)
            }
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        binding.run {
            gvAttendanceRecord.setOnClickListener {
                val action = StudentTabContainerFragmentDirections.actionStudentTabContainerFragmentToStudentAttendanceRecord()
                navController.navigate(action)
            }
        }
    }
    private fun setProfilePicture(imageUri: Uri) {
        Glide.with(this)
            .load(imageUri)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(binding.userProfilePic)
    }

    private fun launchImagePicker() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data!!
            setProfilePicture(selectedImageUri)
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.user.collect {
                binding.tvUserName.text = it.name
                binding.tvUserEmail.text = it.email
                binding.tvUserClass.text = it.classes
            }
        }
        lifecycleScope.launch {
            viewModel.profileUri.collect {
                Glide.with(requireContext())
                    .load(it)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.userProfilePic)
            }
        }
    }

    private fun onProfileUpdated(name: String?, profilePicUri: Uri?) {
        viewModel.updateProfile(name, profilePicUri)
        profilePicUri?.let { setProfilePicture(it) }
    }
}
