package com.akirachix.totosteps.activity

import android.content.Intent
import android.os.Bundle
import com.akirachix.totosteps.R
import androidx.appcompat.app.AppCompatActivity
import com.akirachix.totosteps.ResourcesFragment
import com.akirachix.totosteps.databinding.ActivityViewAutismResultsBinding
class ViewAutismResultsActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewAutismResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAutismResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle back navigation
        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.viewMilestonesButton.setOnClickListener {
            startActivity(Intent(this, HomeScreenActivity::class.java))
        }

//        // Set up click listener for viewing milestones
//        binding.viewMilestonesButton.setOnClickListener {
//            // Create an instance of ResourcesFragment
//            val resourcesFragment = ResourcesFragment()
//
//            // Replace current content with ResourcesFragment
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, resourcesFragment)
//                .addToBackStack(null) // Optional: Add this transaction to back stack
//                .commit()
//        }
//    }
    }
}