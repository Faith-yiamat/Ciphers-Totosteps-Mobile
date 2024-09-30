package com.akirachix.totosteps.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akirachix.totosteps.api.ApiInterface
import com.akirachix.totosteps.databinding.ActivityAutismUploadPhotoBinding
import com.akirachix.totosteps.models.AutismResultResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException

class AutismUploadPhoto : AppCompatActivity() {
    lateinit var binding: ActivityAutismUploadPhotoBinding

    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 101
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutismUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the upload button (image)
        binding.ivUpload.setOnClickListener {
            if (checkStoragePermission()) {
                openImageChooser()
            } else {
                requestStoragePermission()
            }
        }

        // Back button functionality
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        // Submit button functionality
        binding.btnSubmit.setOnClickListener {
            if (selectedImageUri != null) {
                submitImage()
            } else {
                Toast.makeText(this, "Please upload an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to open the image picker
    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    // Handle the image result from the file picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            try {
                // Convert URI to Bitmap and set it to ImageView
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                binding.ivUpload.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to submit the selected image
    private fun submitImage() {
        selectedImageUri?.let { uri ->
            val file = getFileFromUri(uri)

            if (file != null && file.exists()) {
                // Upload image file
                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, requestBody)
                // Call your API here to upload the image
                uploadImage(file) // This is your upload function
            } else {
                Toast.makeText(this, "File not found or cannot be opened", Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(this, "Please upload an image first", Toast.LENGTH_SHORT).show()
    }

    // Method to retrieve the file path from URI
    private fun getFileFromUri(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex!!)
        cursor?.close()

        return filePath?.let { File(it) }
    }

    // Method to check storage permission
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    // Method to request storage permission
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }

    // Handle the permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun uploadImage(file: File) {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://totosteps-29a482165136.herokuapp.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiInterface::class.java)

        // Prepare image file
        val requestFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        // Call the API
        val call = apiService.uploadImage(body)
        call.enqueue(object : Callback<AutismResultResponse> {
            override fun onResponse(call: Call<AutismResultResponse>, response: Response<AutismResultResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    Toast.makeText(this@AutismUploadPhoto, "Result: $result", Toast.LENGTH_LONG).show()
                    // Optionally, move to the next activity to show results
                } else {
                    Toast.makeText(this@AutismUploadPhoto, "Failed to get result", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<AutismResultResponse>, t: Throwable) {
                Toast.makeText(this@AutismUploadPhoto, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
