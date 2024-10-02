package com.akirachix.totosteps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.akirachix.totosteps.api.ApiClient
import com.akirachix.totosteps.databinding.FragmentResourcesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResourcesFragment : Fragment() {
    private lateinit var binding: FragmentResourcesBinding
    private lateinit var resourceAdapter: ResourceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResourcesBinding.inflate(inflater, container, false)
        return binding.root
    }
//    viewModel.resources.observe(viewLifecycleOwner) { resourceList ->
//        resourceAdapter.updateResources(resourceList)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resourceAdapter = ResourceAdapter(listOf())
        binding.rvChildren.layoutManager = LinearLayoutManager(context)
        binding.rvChildren.adapter = resourceAdapter

        fetchResources()
    }

    private fun fetchResources() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.getResources()
                if (response.isSuccessful) {
                    val resources = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        resourceAdapter.updateResources(resources)
                    }
                } else {
                    // Handle API error
                    Log.e("API_ERROR", "Failed to fetch resources: ${response.message()}")
                }
            } catch (e: Exception) {
                // Handle network error
                Log.e("NETWORK_ERROR", "Error fetching resources: $e")
            }
        }
    }
}