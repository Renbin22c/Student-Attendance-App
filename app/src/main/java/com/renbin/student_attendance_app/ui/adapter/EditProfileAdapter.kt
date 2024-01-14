package com.renbin.student_attendance_app.ui.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.core.service.StorageService
import kotlinx.coroutines.launch

class EditProfileAdapter(
    private val fragment: Fragment,
    private val onProfileUpdated: (name: String?, profilePicUri: Uri?) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var imageView: ShapeableImageView
    private var imageUri: Uri? = null
    private var name: String? = null
    private val PICK_IMAGE_REQUEST = 1

    private val storageService = StorageService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_profile_btm_sheet, container, false)

        imageView = view.findViewById(R.id.editImage)
        val editName: EditText = view.findViewById(R.id.etUserName)
        val saveButton: Button = view.findViewById(R.id.btnSave)

        lifecycleScope.launch {
            val loadedImageUri = storageService.loadSelectedImageUri(name.toString())
            if (loadedImageUri != null) {
                imageUri = loadedImageUri
                imageView.setImageURI(loadedImageUri)
            }
        }

        imageView.setOnClickListener {
            openGallery()
        }

        saveButton.setOnClickListener {
            name = editName.text.toString()

            lifecycleScope.launch {
                storageService.saveSelectedImageUri(name.toString(), imageUri)
            }

            onProfileUpdated(name, imageUri)

            dismiss()
        }
        return view
    }

    private fun openGallery() {
        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data!!
            if (imageUri != null) {
                imageView.setImageURI(imageUri)
            }
        }
    }
}
