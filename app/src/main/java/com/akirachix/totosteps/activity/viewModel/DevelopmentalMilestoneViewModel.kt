package com.akirachix.totosteps.activity.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akirachix.totosteps.api.ApiClient
import com.akirachix.totosteps.api.ApiInterface
import com.akirachix.totosteps.models.Question
import kotlinx.coroutines.launch

class DevelopmentalMilestoneViewModel : ViewModel() {
    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val apiService: ApiInterface by lazy {
        ApiClient.instance
    }

    // Fetch questions and filter by milestone
    fun fetchQuestions(category: String, milestone: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Fetch all questions for the given category
                val response = apiService.getQuestions(category)

                // Filter questions by the specified milestone
                val filteredQuestions = response.filter { it.milestone == milestone }

                // Simplify the filtered questions
                val simplifiedQuestions = filteredQuestions.map {
                    Question(
                        id = it.id,
                        questionJson = it.questionJson,
                        milestone = it.milestone,
                        question_type = it.question_type,
                        category = it.category
                    )
                }

                // Post the filtered and simplified list to LiveData
                _questions.postValue(simplifiedQuestions)
                _isLoading.postValue(false)
            } catch (e: Exception) {
                // Handle error and update LiveData
                _error.postValue("Error fetching questions: ${e.message}")
                _isLoading.postValue(false)
            }
        }
    }
}



