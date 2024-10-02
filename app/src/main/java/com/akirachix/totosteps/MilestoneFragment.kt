package com.akirachix.totosteps

import android.content.Intent // Make sure to import this
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.akirachix.totosteps.api.ApiClient
import com.akirachix.totosteps.databinding.FragmentMilestoneBinding
import com.akirachix.totosteps.models.Milestone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MilestoneFragment : Fragment() {
    private lateinit var binding: FragmentMilestoneBinding
    private lateinit var milestonesAdapter: MilestoneAdapter
    private val milestones = mutableListOf<Milestone>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMilestoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchMilestones()
    }

    private fun setupRecyclerView() {
        binding.rvChildren.layoutManager = LinearLayoutManager(requireActivity())
        milestonesAdapter = MilestoneAdapter(milestones) { milestone ->
            navigateToDetail(milestone)
        }
        binding.rvChildren.adapter = milestonesAdapter
    }

    private fun fetchMilestones() {
        ApiClient.instance.getMilestones().enqueue(object : Callback<List<Milestone>> {
            override fun onResponse(call: Call<List<Milestone>>, response: Response<List<Milestone>>) {
                if (response.isSuccessful) {
                    val milestoneList = response.body() ?: emptyList()
                    updateMilestoneData(milestoneList)
                } else {
                    Log.e("API Error", response.message())
                }
            }

            override fun onFailure(call: Call<List<Milestone>>, t: Throwable) {
                Log.e("Network Error", t.message ?: "Unknown error")
            }
        })
    }

    private fun updateMilestoneData(milestones: List<Milestone>) {
        this.milestones.clear()
        this.milestones.addAll(milestones)
        milestonesAdapter.notifyDataSetChanged()
    }

    private fun navigateToDetail(milestone: Milestone) {
        val intent = Intent(requireContext(), MilestoneDetailActivity::class.java).apply {
            putExtra("MILESTONE_ID", milestone.milestone_id)
            putExtra("CATEGORY", milestone.category)
            putExtra("SUMMARY", milestone.summary.joinToString("\n"))
            putExtra("MILESTONE_NAME", milestone.name) // Pass the name
        }
        startActivity(intent)
    }
}
