package com.akirachix.totosteps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.akirachix.totosteps.api.ApiClient
import com.akirachix.totosteps.databinding.ActivityMilestoneResourcesBinding
import com.akirachix.totosteps.models.Milestone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MilestoneResources : AppCompatActivity() {

    private lateinit var binding: ActivityMilestoneResourcesBinding
    private lateinit var milestoneAdapter: MilestoneAdapter
    private val milestoneList = mutableListOf<Milestone>() // Initialize as mutable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMilestoneResourcesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchMilestones()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        milestoneAdapter = MilestoneAdapter(milestoneList) { milestone ->
            navigateToDetail(milestone)
        }
        binding.rvMilestones.layoutManager = LinearLayoutManager(this)
        binding.rvMilestones.adapter = milestoneAdapter
    }

    private fun fetchMilestones() {
        ApiClient.instance.getMilestones().enqueue(object : Callback<List<Milestone>> {
            override fun onResponse(call: Call<List<Milestone>>, response: Response<List<Milestone>>) {
                if (response.isSuccessful) {
                    response.body()?.let { milestones ->
                        milestoneList.clear()
                        milestoneList.addAll(milestones.filter { it.age == 2 }) // Filter by age
                        milestoneAdapter.notifyDataSetChanged()
                    } ?: run {
                        Log.e("API Error", "Response body is null")
                    }
                } else {
                    Log.e("API Error", response.message())
                }
            }

            override fun onFailure(call: Call<List<Milestone>>, t: Throwable) {
                Log.e("Network Error", t.message ?: "Unknown error")
            }
        })
    }

    private fun navigateToDetail(milestone: Milestone) {
        val intent = Intent(this, MilestoneDetailActivity::class.java).apply {
            putExtra("MILESTONE_ID", milestone.milestone_id)
            putExtra("CATEGORY", milestone.category)
            putExtra("SUMMARY", milestone.summary.joinToString("\n"))
        }
        startActivity(intent)
    }


}
