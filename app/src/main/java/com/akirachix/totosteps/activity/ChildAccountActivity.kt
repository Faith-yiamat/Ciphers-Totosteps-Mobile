package com.akirachix.totosteps.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildsAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ChildViewModel::class.java)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
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

        viewModel.createChild(username, dateOfBirth)
    }

    private fun observeViewModel() {
        viewModel.creationStatus.observe(this) { status ->
            when (status) {
                is CreationStatus.Loading -> {
                    // Show loading indicator if needed
                }
                is CreationStatus.Success -> {
                    Toast.makeText(this, "Child profile created successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AutismUploadPhoto::class.java)
                    startActivity(intent)
                    finish() // Close this activity
                }
                is CreationStatus.Error -> {
                    Toast.makeText(this, "Error: ${status.message}", Toast.LENGTH_SHORT).show()
                    if (status.message.contains("Unauthorized")) {
                        // Navigate back to login screen if unauthorized
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}