package com.akirachix.totosteps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akirachix.totosteps.activity.viewModel.ChildrenViewModel
import com.akirachix.totosteps.databinding.FragmentChildrenBinding
import androidx.lifecycle.ViewModel
import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
//import com.akirachix.totosteps.activity.viewModel.ChildrenViewModel
//import com.akirachix.totosteps.databinding.FragmentChildrenBinding
//import kotlinx.coroutines.launch
//
//class ChildrenFragment : Fragment() {
//    private lateinit var binding: FragmentChildrenBinding
//    private lateinit var viewModel: ChildrenViewModel
//    private val _children = MutableLiveData<List<ChildrenDataClass>>()
//    val children: LiveData<List<ChildrenDataClass>> = _children
//
//    private val _error = MutableLiveData<String?>()
//    val error: LiveData<String?> = _error
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize ViewModel
//        viewModel = ViewModelProvider(this).get(ChildrenViewModel::class.java)
//
//        setupRecyclerView()
//        setupAddButton()
//        observeViewModel()
//
//        // Fetch children for the current parent
//        val parentId = getUserIdFromSharedPreferences()
//        Log.d("ChildrenFragment", "Fetching children for parent ID: $parentId")
//
//        // Call the suspend function from coroutine scope
//        viewLifecycleOwner.lifecycleScope.launch {
//            try {
//                val childrenList = viewModel.fetchChildren(parentId)
//                _children.value = childrenList
//            } catch (e: Exception) {
//                _error.value = "Error fetching children: ${e.message}"
//            }
//        }
//    }
//
//    private fun setupRecyclerView() {
//        binding.rvChildren.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        binding.rvChildren.adapter = ChildrenAdapterClass(viewModel.children.value ?: emptyList())
//        Log.d("ChildrenFragment", "Setting up RecyclerView adapter with ${viewModel.children.value?.size} items")
//    }
//
//    private fun setupAddButton() {
//        binding.btnAdd.setOnClickListener {
//            // Navigate to add child screen
//        }
//    }
//
//    private fun observeViewModel() {
//        viewModel.children.observe(viewLifecycleOwner) { children ->
//            Log.d("ChildrenFragment", "Received children data: ${children.size}")
//            binding.rvChildren.adapter = ChildrenAdapterClass(children)
//        }
//
//        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
//            Log.e("ChildrenFragment", "Error message received: $errorMessage")
//        }
//    }
//
//    private fun getUserIdFromSharedPreferences(): Int {
//        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)
//        val userId = sharedPreferences.getInt("USER_ID", -1)
//        Log.d("ChildrenFragment", "Retrieved user ID: $userId")
//        return userId
//    }
//}


import android.widget.Toast



class ChildrenFragment : Fragment() {

    lateinit var binding: FragmentChildrenBinding

    lateinit var childrenAdapter: ChildrenAdapterClass
    val children = mutableListOf<ChildrenDataClass>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChildrenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupAddButton()
        displayChildren()
    }

    fun setupRecyclerView() {
        binding.rvChildren.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        childrenAdapter = ChildrenAdapterClass(children)
        binding.rvChildren.adapter = childrenAdapter
    }

    fun setupAddButton() {
        binding.btnAdd.setOnClickListener {
            addNewChild()
        }
    }

    fun displayChildren() {
        children.clear()
        children.addAll(listOf(
            ChildrenDataClass("", "Muthoni_kayaba", "2years 10months old"),
            ChildrenDataClass("", "Zach_arthur", "2years 10months old"),
            ChildrenDataClass("", "Muthoni_kayaba", "2years 10months old")
        ))
        childrenAdapter.notifyDataSetChanged()
    }

    fun addNewChild() {
        val newChild = ChildrenDataClass("", "New Child", "Age unknown")
        children.add(newChild)
        childrenAdapter.notifyItemInserted(children.size - 1)
        Toast.makeText(requireContext(), "New child added", Toast.LENGTH_SHORT).show()
    }


}