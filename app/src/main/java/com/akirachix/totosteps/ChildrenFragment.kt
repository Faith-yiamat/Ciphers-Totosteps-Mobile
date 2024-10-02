package com.akirachix.totosteps

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.akirachix.totosteps.databinding.FragmentChildrenBinding


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