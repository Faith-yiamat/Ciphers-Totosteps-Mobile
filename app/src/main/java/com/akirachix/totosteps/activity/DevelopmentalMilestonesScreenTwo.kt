


package com.akirachix.totosteps.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.akirachix.totosteps.activity.viewModel.DevelopmentalMilestoneViewModel
import com.akirachix.totosteps.databinding.ActivityDevelopmentalMilestonesScreenTwoBinding
import com.akirachix.totosteps.models.Question
import com.akirachix.totosteps.models.QuestionsAdapter

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
        viewModel.fetchQuestions("Social",1)


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
        // Handle "Back" button click
        binding.btnBackFour.setOnClickListener {
            finish()
        }

        // Handle "Next" button click
        binding.btnNextFour.setOnClickListener {
            if (allQuestionsAnswered()) {
                val intent = Intent(this, DevelopmentalMilestonesScreenThree::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Check if all questions have been answered
    private fun allQuestionsAnswered(): Boolean {
        return viewModel.questions.value?.all { it.answer != null } == true
    }
}