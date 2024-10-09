//package com.akirachix.totosteps
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.akirachix.totosteps.databinding.FragmentChildrenBinding
//import com.akirachix.totosteps.activity.viewModel.ChildrenViewModel
//
//
//
//class ChildrenFragment : Fragment() {
//    private var _binding: FragmentChildrenBinding? = null
//    private val binding get() = _binding!!
//
//    private val viewModel: ChildrenViewModel by viewModels()
//    private lateinit var adapter: ChildrenAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentChildrenBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupRecyclerView()
//        observeViewModel()
//        viewModel.loadChildren()
//    }
//
//    private fun setupRecyclerView() {
//        adapter = ChildrenAdapter()
//        binding.rvChildren.apply {
//            adapter = this@ChildrenFragment.adapter
//            layoutManager = LinearLayoutManager(context)
//        }
//    }
//
//    private fun observeViewModel() {
//        viewModel.children.observe(viewLifecycleOwner) { children ->
//            adapter.updateChildren(children)
//        }
//
//        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
//            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

package com.akirachix.totosteps

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.akirachix.totosteps.databinding.FragmentChildrenBinding
import com.akirachix.totosteps.activity.viewModel.ChildrenViewModel
import com.akirachix.totosteps.activity.viewModel.ChildrenViewModelFactory
import com.akirachix.totosteps.api.ApiClient
import com.akirachix.totosteps.activity.ChildAccountActivity
import android.content.Context

class ChildrenFragment : Fragment() {
    private var _binding: FragmentChildrenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChildrenViewModel by viewModels {
        ChildrenViewModelFactory(
            ChildrenRepository(
                apiService = ApiClient.instance,
                sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            )
        )
    }
    private lateinit var adapter: ChildrenAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildrenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ApiClient.initialize(requireContext())

        setupRecyclerView()
        setupFab()
        observeViewModel()
        viewModel.loadChildren()
    }

    private fun setupRecyclerView() {
        adapter = ChildrenAdapter()
        binding.rvChildren.apply {
            adapter = this@ChildrenFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupFab() {
        binding.fabAddChild.setOnClickListener {
            val intent = Intent(requireContext(), ChildAccountActivity::class.java)
            startActivityForResult(intent, ADD_CHILD_REQUEST)
        }
    }

    private fun observeViewModel() {
        viewModel.children.observe(viewLifecycleOwner) { children ->
            adapter.updateChildren(children)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CHILD_REQUEST && resultCode == Activity.RESULT_OK) {
            viewModel.refreshChildren()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ADD_CHILD_REQUEST = 1
    }
}