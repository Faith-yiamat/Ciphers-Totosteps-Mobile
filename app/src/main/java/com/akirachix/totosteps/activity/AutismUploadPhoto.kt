package com.akirachix.totosteps.activity

//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import android.graphics.Bitmap
//import android.net.Uri
//import android.provider.MediaStore
//import android.widget.Toast
//import java.io.IOException
//import android.view.View
//import androidx.activity.viewModels
//import androidx.lifecycle.lifecycleScope
//import com.akirachix.totosteps.activity.viewModel.AutismUploadViewModel
//import com.akirachix.totosteps.activity.viewModel.UploadState
//import com.akirachix.totosteps.databinding.ActivityAutismUploadPhotoBinding
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//import java.io.File
//import java.io.FileOutputStream
//
//class AutismUploadPhoto : AppCompatActivity() {
//    private lateinit var binding: ActivityAutismUploadPhotoBinding
//    private val viewModel: AutismUploadViewModel by viewModels()
//
//    private val PICK_IMAGE_REQUEST = 1
//    private var selectedImageUri: Uri? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAutismUploadPhotoBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.ivUpload.setOnClickListener { openImageChooser() }
//        binding.ivBack.setOnClickListener { onBackPressed() }
//        binding.btnSubmit.setOnClickListener { submitImage() }
//
//        observeUploadState()
//    }
//
//    private fun observeUploadState() {
//        lifecycleScope.launch {
//            viewModel.uploadState.collectLatest { state ->
//                when (state) {
//                    is UploadState.Idle -> {
//                        binding.btnSubmit.isEnabled = true
//                    }
//                    is UploadState.Loading -> {
//                        binding.btnSubmit.isEnabled = false
//                    }
//                    is UploadState.Success -> {
//                        showSuccessOverlay()
//                        navigateToResults(state.response.id)
//                    }
//                    is UploadState.Error -> {
//                        binding.btnSubmit.isEnabled = true
//                        Toast.makeText(this@AutismUploadPhoto, "Upload failed: ${state.message}", Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }
//    }
//
//    private fun openImageChooser() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
//            selectedImageUri = data.data
//            try {
//                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
//                binding.ivUpload.setImageBitmap(bitmap)
//                binding.btnSubmit.isEnabled = true
//            } catch (e: IOException) {
//                e.printStackTrace()
//                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun submitImage() {
//        selectedImageUri?.let { uri ->
//            val file = getFileFromUri(uri)
//            file?.let {
//                viewModel.uploadImage(it)
//            } ?: run {
//                Toast.makeText(this, "Failed to process the image", Toast.LENGTH_SHORT).show()
//            }
//        } ?: run {
//            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun getFileFromUri(uri: Uri): File? {
//        return try {
//            val inputStream = contentResolver.openInputStream(uri)
//            val file = File(cacheDir, "temp_image")
//            inputStream?.use { input ->
//                FileOutputStream(file).use { output ->
//                    input.copyTo(output)
//                }
//            }
//            file
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    private fun showSuccessOverlay() {
//        binding.btnSubmit.visibility = View.GONE
//        binding.successOverlay.visibility = View.VISIBLE
//    }
//
//    private fun navigateToResults(resultId: String) {
//        binding.successOverlay.postDelayed({
//            binding.successOverlay.visibility = View.GONE
//            val intent = Intent(this, ViewAutismResultsActivity::class.java).apply {
//                putExtra("RESULT_ID", resultId)
//            }
//            startActivity(intent)
//            finish()
//        }, 2000)
//    }
//}

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import java.io.IOException
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akirachix.totosteps.activity.viewModel.AutismUploadViewModel
import com.akirachix.totosteps.activity.viewModel.UploadState
import com.akirachix.totosteps.databinding.ActivityAutismUploadPhotoBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class AutismUploadPhoto : AppCompatActivity() {
    private lateinit var binding: ActivityAutismUploadPhotoBinding
    private val viewModel: AutismUploadViewModel by viewModels()

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private var childId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutismUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the child ID from the intent
        childId = intent.getIntExtra("CHILD_ID", -1)
        if (childId == -1) {
            Toast.makeText(this, "Error: Child ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.ivUpload.setOnClickListener { openImageChooser() }
        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.btnSubmit.setOnClickListener { submitImage() }

        observeUploadState()
    }

    private fun observeUploadState() {
        lifecycleScope.launch {
            viewModel.uploadState.collectLatest { state ->
                when (state) {
                    is UploadState.Idle -> {
                        binding.btnSubmit.isEnabled = true
                    }
                    is UploadState.Loading -> {
                        binding.btnSubmit.isEnabled = false
                        // Show loading indicator if needed
                    }
                    is UploadState.Success -> {
                        navigateToResults(state.response.results_id)
                    }
                    is UploadState.Error -> {
                        binding.btnSubmit.isEnabled = true
                        Toast.makeText(this@AutismUploadPhoto, "Upload failed: ${state.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                binding.ivUpload.setImageBitmap(bitmap)
                binding.btnSubmit.isEnabled = true
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitImage() {
        selectedImageUri?.let { uri ->
            val file = getFileFromUri(uri)
            file?.let {
                viewModel.uploadImage(childId, it)
            } ?: run {
                Toast.makeText(this, "Failed to process the image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(cacheDir, "temp_image")
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun navigateToResults(resultId: Int) {
        val intent = Intent(this, ViewAutismResultsActivity::class.java).apply {
            putExtra("RESULT_ID", resultId)
        }
        startActivity(intent)
        finish()
    }
}