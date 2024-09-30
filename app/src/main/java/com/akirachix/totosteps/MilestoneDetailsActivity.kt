package com.akirachix.totosteps

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MilestoneDetailActivity : AppCompatActivity() {
    private lateinit var tvMilestoneName: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvSummary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_details)

        tvMilestoneName = findViewById(R.id.tvMilestoneName)
        tvCategory = findViewById(R.id.tvCategory)
        tvSummary = findViewById(R.id.tvSummary)

        // Retrieve the data from the intent
        val milestoneId = intent.getIntExtra("MILESTONE_ID", -1)
        val category = intent.getStringExtra("CATEGORY") ?: "N/A"
        val summary = intent.getStringExtra("SUMMARY") ?: "No details available"

        // Assuming you have a way to fetch the milestone name using the ID or it's passed directly
        // For example, if you passed the name directly:
        val milestoneName = intent.getStringExtra("MILESTONE_NAME") ?: "Unknown Milestone"

        // Set the values in the TextViews
        tvMilestoneName.text = milestoneName
        tvCategory.text = category
        tvSummary.text = summary
    }
}
