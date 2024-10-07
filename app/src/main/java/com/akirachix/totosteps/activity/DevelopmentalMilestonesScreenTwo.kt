package com.akirachix.totosteps.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.akirachix.totosteps.databinding.ActivityDevelopmentalMilestonesScreenTwoBinding
import com.akirachix.totosteps.models.Question
import com.akirachix.totosteps.models.QuestionsAdapter
import com.akirachix.totosteps.activity.viewModel.DevelopmentalMilestoneViewModel
import com.akirachix.totosteps.api.ApiClient
import com.akirachix.totosteps.api.ResultData
import com.akirachix.totosteps.api.ResultResponse
import retrofit2.Call

class DevelopmentalMilestonesScreenTwo : AppCompatActivity() {
    private lateinit var binding: ActivityDevelopmentalMilestonesScreenTwoBinding
    private lateinit var viewModel: DevelopmentalMilestoneViewModel
    private lateinit var adapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevelopmentalMilestonesScreenTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(DevelopmentalMilestoneViewModel::class.java)

        // Initialize Adapter with an empty list
        adapter = QuestionsAdapter(emptyList())

        // Setup RecyclerView
        binding.rvChildren.layoutManager = LinearLayoutManager(this)
        binding.rvChildren.adapter = adapter

        // Observe ViewModel data
        observeViewModel()

        // Fetch specific question by ID (e.g., question_id = 19)
        viewModel.fetchQuestions("Social", 1)


        setupUi()
    }

    private fun observeViewModel() {
        viewModel.questions.observe(this) { questions ->
            Log.d("DevelopmentalMilestonesScreenTwo", "Received ${questions.size} questions")

            // Update adapter with new questions
            if (questions.isNotEmpty()) {
                adapter.questions = questions
                adapter.notifyDataSetChanged()
            } else {
                Log.e("DevelopmentalMilestonesScreenTwo", "No questions received")
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Show or hide progress bar based on loading state
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            // Display error message as a toast
            Log.e("DevelopmentalMilestonesScreenTwo", "Error: $errorMessage")
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupUi() {
        binding.btnBackFour.setOnClickListener {
            finish()
        }

        // Handle "Next" button click
//        binding.btnNextFour.setOnClickListener {
        binding.btnNextFour.setOnClickListener {
            if (allQuestionsAnswered()) {
                val userId = getUserIdFromSharedPreferences()

                if (userId != -1) {
                    Log.d("DevelopmentalMilestonesScreenTwo", "User ID: $userId")

                    // Get user answers
                    val answers = collectAnswers()

                    // Retrieve the milestone ID dynamically (you could fetch it from intent or use default)
                    val milestoneId = 1
                    if (milestoneId != -1) {
                        // Prepare the result data
                        val resultData = ResultData(
                            milestone = milestoneId,
                            answers = answers,
                            user = userId
                        )

                        // Make the API call to submit the result
                        submitResult(resultData)

                        // Navigate to DevelopmentalMilestoneScreenThree after submission
                        val intent =
                            Intent(this, DevelopmentalMilestonesScreenThree::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Milestone ID not provided", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()
            }
//            }

        }
    }

    // Check if all questions have been answered
    private fun allQuestionsAnswered(): Boolean {
        return viewModel.questions.value?.all { it.answer != null } == true
    }

    private fun collectAnswers(): Map<String, String> {
        val answersMap = mutableMapOf<String, String>()

        viewModel.questions.value?.forEach { question ->
            question.answer?.let { answer ->
                answersMap[question.questionJson] =
                    answer.toString() // Assuming "question" is the question text
            }
        }

        return answersMap
    }

    private fun submitResult(resultData: ResultData) {
        val call = ApiClient.instance.submitResult(resultData)

        call.enqueue(object : retrofit2.Callback<ResultResponse> {
            override fun onResponse(
                call: Call<ResultResponse>,
                response: retrofit2.Response<ResultResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@DevelopmentalMilestonesScreenTwo,
                        "Good job! Continue",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@DevelopmentalMilestonesScreenTwo,
                        "Failed to submit result",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                Toast.makeText(
                    this@DevelopmentalMilestonesScreenTwo,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        return sharedPreferences.getInt("USER_ID", -1) // Default to -1 if not found
    }

}