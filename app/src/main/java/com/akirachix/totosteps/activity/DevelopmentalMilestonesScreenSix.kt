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

    private fun setupUi() {
        // Handle "Back" button click
        binding.btnBackFour.setOnClickListener {
            finish()
        }

        // Handle "Next" button click (Submit)
        binding.btnNextFour.setOnClickListener {
            if (allQuestionsAnswered()) {
                showAssessmentResultsDialog()
            } else {
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Check if all questions have been answered in the RecyclerView
    private fun allQuestionsAnswered(): Boolean {
        return viewModel.questions.value?.all { it.answer != null } == true
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
        val intent = Intent(this, HomeScreenActivity::class.java) // Replace with your actual homepage activity class.
        startActivity(intent)
        finish() // Optionally finish this activity so it's removed from the back stack.
    }
}





