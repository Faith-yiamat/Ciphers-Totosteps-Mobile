package com.akirachix.totosteps.activity.viewModel

import com.akirachix.totosteps.api.ApiClient

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.akirachix.totosteps.ChildrenDataClass
//import com.akirachix.totosteps.api.ApiClient
//
////import android.util.Log
////import androidx.lifecycle.LiveData
////import androidx.lifecycle.MutableLiveData
////import androidx.lifecycle.ViewModel
////import androidx.lifecycle.viewModelScope
////import com.akirachix.totosteps.ChildrenDataClass
////import com.akirachix.totosteps.api.ApiClient
////import kotlinx.coroutines.Dispatchers
////import kotlinx.coroutines.launch
////import kotlinx.coroutines.withContext
////
////class ChildrenViewModel : ViewModel() {
////    private val _children = MutableLiveData<List<ChildrenDataClass>>()
////    val children: LiveData<List<ChildrenDataClass>> = _children
////
////    private val _error = MutableLiveData<String?>()
////    val error: LiveData<String?> = _error
////
////    suspend fun fetchChildren(parentId: Int): List<ChildrenDataClass> {
////        return withContext(Dispatchers.IO) {
////            try {
////                val response = ApiClient.instance.getChildrenByParent(parentId)
////                Log.d("ChildViewModel", "API call returned status code: ${response.code()}")
////
////                if (response.isSuccessful && response.body() != null) {
////                    val body = response.body()
////
////                    // Check if the body is an array or an object
////                    if (body is Array<*>) {
////                        // Parse the array
////                        body.map { child: Any ->
////                            ChildrenDataClass(
////                                avatar = (child as Map<*, *>).getOrDefault("avatar", "") as String,
////                                name = (child as Map<*, *>).getOrDefault("name", "") as String,
////                                age = (child as Map<*, *>).getOrDefault("age", "") as String
////                            )
////                        }
////                    } else if (body is Map<*, *>) {
////                        // Parse the nested object
////                        val childrenArray = body["children"] as Array<*>
////                        childrenArray.map { child ->
////                            ChildrenDataClass(
////                                avatar = (child as Map<*, *>).getOrDefault("avatar", "") as String,
////                                name = (child as Map<*, *>).getOrDefault("name", "") as String,
////                                age = (child as Map<*, *>).getOrDefault("age", "") as String
////                            )
////                        }
////                    } else {
////                        throw RuntimeException("Unexpected response format")
////                    }
////
////                    Log.d("ChildViewModel", "Successfully fetched children data")
////                    return@withContext body.map { child ->
////                        ChildrenDataClass(
////                            avatar = (child as Map<*, *>).getOrDefault("avatar", "") as String,
////                            name = (child as Map<*, *>).getOrDefault("name", "") as String,
////                            age = (child as Map<*, *>).getOrDefault("age", "") as String
////                        )
////                    }
////                } else {
////                    Log.e("ChildViewModel", "Failed to fetch children: ${response.errorBody()?.string()}")
////                    throw Exception("Failed to fetch children: ${response.errorBody()?.string()}")
////                }
////            } catch (e: Exception) {
////                Log.e("ChildViewModel", "Error fetching children: ${e.message}")
////                throw Exception("Error fetching children: ${e.message}")
////            }
////        }
////    }
////}
//
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.math.abs
//
//class ChildrenViewModel : ViewModel() {
//
//    private val _childrenList = MutableLiveData<List<ChildrenDataClass>>()
//    val childrenList: LiveData<List<ChildrenDataClass>> get() = _childrenList
//
//    fun fetchChildren() {
//        viewModelScope.launch {
//            try {
//                val response = ApiClient.instance.getChildrenByParent(16)
//                val children = response.children.map { child ->
//                    val ageInMonths = calculateAgeInMonths(child.dateOfBirth)
//                    ChildrenDataClass(
//                        name = child.username,
//                        age = "$ageInMonths months"
//                    )
//                }
//                _childrenList.value = children
//            } catch (e: Exception) {
//                // Handle the error
//            }
//        }
//    }
//
//    private fun fetchChildrenFromApi(): ParentResponse {
//        // Simulate an API call. Replace this with the actual API call.
//        return mockParentResponse() // Mocked response.
//    }
//
//    private fun calculateAgeInMonths(dateOfBirth: String): Int {
//        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        val dob = sdf.parse(dateOfBirth)
//        val today = Date()
//        val diffInMillis = abs(today.time - dob.time)
//        val months = (diffInMillis / (1000L * 60 * 60 * 24 * 30))
//        return months.toInt()
//    }
//
//    private fun mockParentResponse(): ParentResponse {
//        // Example mocked response. Replace this with your API response.
//        return ParentResponse(
//            userId = 16,
//            children = listOf(
//                Child( username = "sima", dateOfBirth = "2023-10-04"),
//                Child( username = "Latifa", dateOfBirth = "2023-10-05"),
//                // Add other children here...
//            )
//        )
//    }
//}
//
//
//data class ParentResponse(
//    val userId: Int,
//    val children: List<Child>
//)
//data class Child(
//    val username: String,
//    val dateOfBirth: String
//)



import androidx.lifecycle.viewModelScope
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch

class ChildrenViewModel : ViewModel() {
    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children
    fun fetchChildren() {
        viewModelScope.launch {
            try {
                // Assume we have an API service class
                val response = ApiClient.instance.getChildrenByParent(16)
                _children.value = response.children
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
data class ChildrenResponse(
    val userId: Int,
    val children: List<Child>
)

data class Child(
    val childId: Int,
    val username: String,
    val dateOfBirth: String,
    val age: Int,  // New field
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val parent: Int
)



