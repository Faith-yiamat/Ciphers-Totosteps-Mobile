package com.akirachix.totosteps.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akirachix.totosteps.api.ApiClient
import com.akirachix.totosteps.api.ApiInterface
import com.akirachix.totosteps.resources
import kotlinx.coroutines.launch
import retrofit2.Response

class ResourceViewModel : ViewModel() {

    private val _resources = MutableLiveData<List<resources>>()
    val resources: LiveData<List<resources>> = _resources

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val apiService: ApiInterface by lazy {
        ApiClient.instance
    }

    fun fetchResources() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<List<resources>> = apiService.getResources()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _resources.postValue(it)
                    }
                } else {
                    _error.postValue("Failed to fetch resources. Error code:${response.code()}")
                }
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _error.postValue("Error fetching resources: ${e.message}")
                _isLoading.postValue(false)
            }

        }
    }
//    }
//    fun getResources(): LiveData<List<resources>>{
//        val resourcesLiveData
//        return resourcesLiveData
//    }


}
