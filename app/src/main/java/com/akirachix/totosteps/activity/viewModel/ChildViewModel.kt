package com.akirachix.totosteps.activity.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akirachix.totosteps.api.ApiClient
import com.akirachix.totosteps.api.ApiInterface
import com.akirachix.totosteps.models.ChildData
import kotlinx.coroutines.launch

class ChildViewModel : ViewModel() {
    private val _hasChild = MutableLiveData<Boolean>()
    val hasChild: LiveData<Boolean> = _hasChild

    private val _creationStatus = MutableLiveData<CreationStatus>()
    val creationStatus: LiveData<CreationStatus> = _creationStatus

    private val apiService: ApiInterface = ApiClient.instance

    fun createChild(username: String, dateOfBirth: String) {
        viewModelScope.launch {
            _creationStatus.value = CreationStatus.Loading
            try {
                val childData = ChildData(username, dateOfBirth)
                Log.d("ChildViewModel", "Sending child data: $childData")

                val response = apiService.createChild(childData)

                if (response.isSuccessful) {
                    _creationStatus.value = CreationStatus.Success
                    setHasChild(true)
                    Log.d("ChildViewModel", "Child creation successful: ${response.body()}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    _creationStatus.value = CreationStatus.Error("API error: ${response.code()} - $errorBody")
                    Log.e("ChildViewModel", "API error: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                _creationStatus.value = CreationStatus.Error(e.message ?: "Unknown error occurred")
                Log.e("ChildViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun setHasChild(value: Boolean) {
        _hasChild.value = value
    }
}

sealed class CreationStatus {
    object Loading : CreationStatus()
    object Success : CreationStatus()
    data class Error(val message: String) : CreationStatus()
}