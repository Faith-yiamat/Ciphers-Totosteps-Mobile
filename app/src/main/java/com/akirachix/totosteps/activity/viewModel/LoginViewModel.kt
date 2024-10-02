package com.akirachix.totosteps.activity.viewModel
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.akirachix.totosteps.api.ApiClient
//import com.akirachix.totosteps.models.LoginRequest
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//
//class LoginViewModel(application: Application) : AndroidViewModel(application) {
//
//
//
//    private val _loginResult = MutableLiveData<Result<String>>()
//    val loginResult: LiveData<Result<String>> = _loginResult
//
//
//    private val apiService = ApiClient.instance
//
//    // API Login Logic with Coroutines
//    fun login(email: String, password: String) {
//        if (!validateForm(email, password)) return
//
//        val loginRequest = LoginRequest(email, password)
//
//        viewModelScope.launch {
//            try {
//                val response = apiService.loginUser(loginRequest) // Assuming login is a suspend function
//                if (response.isSuccessful && response.body()?.status == "success") {
//                    _loginResult.postValue(Result.success(response.body()?.message ?: "Login successful"))
//                } else {
//                    _loginResult.postValue(Result.failure(Throwable(response.body()?.message ?: "Login failed")))
//                }
//            } catch (e: HttpException) {
//                _loginResult.postValue(Result.failure(Throwable("Network error: ${e.message()}")))
//            } catch (e: Exception) {
//                _loginResult.postValue(Result.failure(Throwable("An error occurred: ${e.localizedMessage}")))
//            }
//        }
//    }
//
//
//
//    // Validation Logic
//    fun validateForm(email: String, password: String): Boolean {
//        return when {
//            email.isEmpty() -> {
//                _loginResult.postValue(Result.failure(Throwable("Username is required")))
//                false
//            }
//            password.isEmpty() -> {
//                _loginResult.postValue(Result.failure(Throwable("Password is required")))
//                false
//            }
//            password.length < 6 -> {
//                _loginResult.postValue(Result.failure(Throwable("Password must be at least 6 characters long")))
//                false
//            }
//            else -> true
//        }
//    }
//}
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akirachix.totosteps.api.ApiClient
import com.akirachix.totosteps.api.ApiInterface
import com.akirachix.totosteps.models.LoginRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> = _loginResult

    private val apiService: ApiInterface? by lazy {
        try {
            ApiClient.instance
        } catch (e: UninitializedPropertyAccessException) {
            _loginResult.postValue(Result.failure(Throwable("API Client not initialized. Please restart the app.")))
            null
        }
    }

    fun login(email: String, password: String) {
        if (!validateForm(email, password)) return

        val loginRequest = LoginRequest(email, password)

        viewModelScope.launch {
            try {
                val api = safeApiCall() ?: return@launch
                val response = api.loginUser(loginRequest)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _loginResult.postValue(Result.success(response.body()?.message ?: "Login successful"))
                } else {
                    _loginResult.postValue(Result.failure(Throwable(response.body()?.message ?: "Login failed")))
                }
            } catch (e: HttpException) {
                _loginResult.postValue(Result.failure(Throwable("Network error: ${e.message()}")))
            } catch (e: Exception) {
                _loginResult.postValue(Result.failure(Throwable("An error occurred: ${e.localizedMessage}")))
            }
        }
    }

    private fun safeApiCall(): ApiInterface? {
        return apiService ?: run {
            _loginResult.postValue(Result.failure(Throwable("API Client not available. Please restart the app.")))
            null
        }
    }

    fun validateForm(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                _loginResult.postValue(Result.failure(Throwable("Email is required")))
                false
            }
            password.isEmpty() -> {
                _loginResult.postValue(Result.failure(Throwable("Password is required")))
                false
            }
            password.length < 6 -> {
                _loginResult.postValue(Result.failure(Throwable("Password must be at least 6 characters long")))
                false
            }
            else -> true
        }
    }
}