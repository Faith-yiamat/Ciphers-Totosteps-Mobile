package com.akirachix.totosteps.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.akirachix.totosteps.databinding.ActivityDevelopmentalMilestonesScreenSixBinding
import com.akirachix.totosteps.models.Question
import com.akirachix.totosteps.models.QuestionsAdapter
import com.akirachix.totosteps.activity.viewModel.DevelopmentalMilestoneViewModel
import com.akirachix.totosteps.api.ApiClient
import com.akirachix.totosteps.api.ResultData
import com.akirachix.totosteps.api.ResultResponse
import retrofit2.Call

class DevelopmentalMilestonesScreenSix : AppCompatActivity() {
    private lateinit var binding: ActivityDevelopmentalMilestonesScreenSixBinding
    private lateinit var viewModel: DevelopmentalMilestoneViewModel
    private lateinit var adapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevelopmentalMilestonesScreenSixBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(DevelopmentalMilestoneViewModel::class.java)

        // Initialize Adapter with an empty list of questions
        adapter = QuestionsAdapter(emptyList())

        // Setup RecyclerView
        binding.rvChildren.layoutManager = LinearLayoutManager(this)
        binding.rvChildren.adapter = adapter

        // Observe ViewModel data
        observeViewModel()

        // Fetch specific questions (example: category "Movement" and milestone 1)
        viewModel.fetchQuestions("Movement", 1)
//        val milestoneId = intent.getIntExtra("MILESTONE_ID", -1) // Default to -1 if not found
//        if (milestoneId != -1) {
//            viewModel.fetchQuestions("Movement", milestoneId) // Use dynamic milestone ID
//        } else {
//            // Handle the error, milestoneId not found
//            Toast.makeText(this, "Milestone ID not provided", Toast.LENGTH_SHORT).show()
//        }

        setupUi()
    }

    private fun observeViewModel() {
        viewModel.questions.observe(this) { questions ->
            Log.d("DevelopmentalMilestonesScreenSix", "Received ${questions.size} questions")

            // Update adapter with new questions
            if (questions.isNotEmpty()) {
                adapter.questions = questions
                adapter.notifyDataSetChanged()
                updateProgressBar()
            } else {
                Log.e("DevelopmentalMilestonesScreenSix", "No questions received")
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Show or hide progress bar based on loading state
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            // Display error message as a toast
            Log.e("DevelopmentalMilestonesScreenSix", "Error: $errorMessage")
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    //    modify the submit button logic to include the API call after collecting the answers
    private fun setupUi() {

        binding.btnNextFour.setOnClickListener {
            if (allQuestionsAnswered()) {
                val userId = getUserIdFromSharedPreferences()

                if (userId != -1) {
                    Log.d("DevelopmentalMilestonesScreenTwo", "User ID: $userId")
                    // Use userId as needed
                } else {
                    Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                }
                val answers = collectAnswers() // Get user answers
//                val userId = 135 // Replace with actual user_id from login/registration
                val milestoneId = 1 // Replace with actual milestone ID

                // Prepare the result data
                val resultData = ResultData(
                    milestone = milestoneId,
                    answers = answers,
                    user = userId
                )

                // Make the API call
                submitResult(resultData)
            } else {
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    // Check if all questions have been answered in the RecyclerView
    private fun allQuestionsAnswered(): Boolean {
        return viewModel.questions.value?.all { it.answer != null } == true
    }


    // Collect user answers for submission
    private fun collectAnswers(): Map<String, String> {
        val answersMap = mutableMapOf<String, String>()

        viewModel.questions.value?.forEach { question ->
            question.answer?.let { answer ->
                answersMap[question.questionJson] = answer.toString() // Assuming "question" is the question text
            }
        }

        return answersMap
    }

    // Update progress bar based on answered questions (if applicable)
    private fun updateProgressBar() {
        val totalQuestions = viewModel.questions.value?.size ?: 0
        val answeredQuestions = viewModel.questions.value?.count { it.answer != null } ?: 0

        val progress = if (totalQuestions > 0) {
            (answeredQuestions.toFloat() / totalQuestions * 100).toInt()
        } else {
            0 // Avoid division by zero if there are no questions.
        }

        binding.progressBar.progress = progress
    }

    //    handle the API request with Retrofit
    private fun submitResult(resultData: ResultData) {
        val call = ApiClient.instance.submitResult(resultData)

        call.enqueue(object : retrofit2.Callback<ResultResponse> {
            override fun onResponse(
                call: Call<ResultResponse>,
                response: retrofit2.Response<ResultResponse>
            ) {
                if (response.isSuccessful) {
                    // Show the existing assessment result dialog
                    showAssessmentResultsDialog()
                } else {
                    Toast.makeText(
                        this@DevelopmentalMilestonesScreenSix,
                        "Failed to submit result",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                Toast.makeText(
                    this@DevelopmentalMilestonesScreenSix,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    // Show dialog to inform user to check images for results and navigate to homepage on OK click
    private fun showAssessmentResultsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Assessment Results")
            .setMessage("Please check your email for assessment results.")
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                navigateToHomePage() // Navigate to homepage after dismissing the dialog.
            }

        val dialog = builder.create()

        // Show the dialog with a dimmed background effect
        dialog.window?.setDimAmount(0.5f) // Dim background to create a light-dark effect.

        dialog.show()
    }

    // Navigate to the homepage activity
    private fun navigateToHomePage() {
        val intent = Intent(
            this,
            HomeScreenActivity::class.java
        ) // Replace with your actual homepage activity class.
        startActivity(intent)
        finish() // Optionally finish this activity so it's removed from the back stack.
    }
    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        return sharedPreferences.getInt("USER_ID", -1) // Default to -1 if not found
    }
}





