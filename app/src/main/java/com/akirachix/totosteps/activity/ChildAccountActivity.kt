package com.akirachix.totosteps.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.akirachix.totosteps.activity.viewModel.ChildViewModel
import com.akirachix.totosteps.activity.viewModel.CreationStatus
import com.akirachix.totosteps.databinding.ActivityChildsAccountBinding
import java.util.Calendar

class ChildAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChildsAccountBinding
    private lateinit var viewModel: ChildViewModel
    private var parent: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildsAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ChildViewModel::class.java)

        parent = getUserIdFromSharedPreferences()
        Log.d("ChildAccountActivity", "Retrieved Parent User ID: $parent")

        if (parent == -1) {
            Log.e("ChildAccountActivity", "Error: Parent user ID not found")
            Toast.makeText(this, "Error: Parent user ID not found", Toast.LENGTH_SHORT).show()
            navigateToLogin()
            return
        }

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.backArrow.setOnClickListener {
            navigateToLogin()
        }

        binding.dateOfBirthInput.setOnClickListener {
            showDatePicker()
        }

        binding.dateOfBirthLayout.setEndIconOnClickListener {
            showDatePicker()
        }

        binding.saveButton.setOnClickListener {
            saveChildData()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            binding.dateOfBirthInput.setText(selectedDate)
        }, year, month, day).show()
    }

    private fun saveChildData() {
        val username = binding.usernameInput.text.toString()
        val dateOfBirth = binding.dateOfBirthInput.text.toString()

        if (username.isEmpty() || dateOfBirth.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (parent == -1) {
            Toast.makeText(this, "Error: Parent user ID not found", Toast.LENGTH_SHORT).show()
            Log.e("ChildAccountActivity", "Attempted to save child data with invalid parent ID")
            return
        }

        Log.d("ChildAccountActivity", "Saving child data - Parent ID: $parent, Username: $username, DOB: $dateOfBirth")
        viewModel.createChild(username, dateOfBirth, true, parent)
    }

    private fun observeViewModel() {
        viewModel.creationStatus.observe(this) { status ->
            when (status) {
                is CreationStatus.Loading -> {
                    // Show loading indicator if needed
                }
                is CreationStatus.Success -> {
                    Log.d("ChildAccountActivity", "Child profile created successfully")
                    Toast.makeText(this, "Child profile created successfully", Toast.LENGTH_SHORT).show()
                    navigateToAutismUploadPhoto()
                }
                is CreationStatus.Error -> {
                    Log.e("ChildAccountActivity", "Error creating child profile: ${status.message}")
                    Toast.makeText(this, "Error: ${status.message}", Toast.LENGTH_SHORT).show()
                    if (status.message.contains("Unauthorized")) {
                        navigateToLogin()
                    }
                }
            }
        }
    }

    private fun navigateToAutismUploadPhoto() {
        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
        finish() // Close this activity
    }

    private fun navigateToLogin() {
        Log.d("ChildAccountActivity", "Navigating back to LoginActivity")
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)
        Log.d("ChildAccountActivity", "Retrieved User ID from SharedPreferences: $userId")
        return userId
    }
}
















