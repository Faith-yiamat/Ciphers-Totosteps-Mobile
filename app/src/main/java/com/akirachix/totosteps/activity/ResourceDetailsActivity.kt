package com.akirachix.totosteps.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.akirachix.totosteps.databinding.ActivityResourceDetailsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ResourceDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityResourceDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResourceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the data passed via intent
        val resourceTitle = intent.getStringExtra("RESOURCE_TITLE") ?: "No Title"
        val resourceTipsJson = intent.getStringExtra("RESOURCE_TIPS") ?: "[]"
        val resourceActivitiesJson = intent.getStringExtra("RESOURCE_ACTIVITIES") ?: "[]"

        // Initialize Gson and define the type for a List<String>
        val gson = Gson()
        val listType = object : TypeToken<List<String>>() {}.type

        // Deserialize JSON into a List<String>
        val resourceTips: List<String> = gson.fromJson(resourceTipsJson, listType)
        val resourcesActivities: List<String> = gson.fromJson(resourceActivitiesJson, listType)

        // Set the resource title
        binding.tvResourceTitle.text = resourceTitle

        // Format tips and activities into text
        val tipsText = resourceTips.joinToString("\n") { it }
        val activitiesText = resourcesActivities.joinToString("\n") { it }

        // Set tips and activities to TextViews
        binding.tvTips.text = tipsText
        binding.tvActivities.text = activitiesText

        // Debugging logs
        Log.d("ResourceDetails", "Tips: $tipsText")
        Log.d("ResourceDetails", "Activities: $activitiesText")
    }
}
