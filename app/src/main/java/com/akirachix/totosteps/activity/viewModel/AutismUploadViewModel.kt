package com.akirachix.totosteps.activity.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

class AutismUploadViewModel : ViewModel() {
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://totosteps-29a482165136.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun uploadImage(imageFile: File) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            try {
                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                val response = apiService.uploadImage(body)
                _uploadState.value = UploadState.Success(response)
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    data class Success(val response: ImageUploadResponse) : UploadState()
    data class Error(val message: String) : UploadState()
}

data class ImageUploadResponse(
    val id: String,
    val resultUrl: String
)

interface ApiService {
    @Multipart
    @POST("/api/results/")
    suspend fun uploadImage(@Part image: MultipartBody.Part): ImageUploadResponse
}