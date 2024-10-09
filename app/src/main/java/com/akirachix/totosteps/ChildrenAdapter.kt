package com.akirachix.totosteps

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.akirachix.totosteps.databinding.ChildrenListViewBinding
import com.akirachix.totosteps.models.Child
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ChildrenAdapter : RecyclerView.Adapter<ChildrenAdapter.ChildViewHolder>() {
    private var children = emptyList<Child>()

    class ChildViewHolder(private val binding: ChildrenListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(child: Child) {
            binding.tvName.text = child.username
            binding.tvAge.text = calculateAge(child.dateOfBirth)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun calculateAge(dateOfBirth: String): String {
            val dob = LocalDate.parse(dateOfBirth)
            val today = LocalDate.now()

            val months = ChronoUnit.MONTHS.between(dob, today)
            val years = months / 12
            val remainingMonths = months % 12

            return when {
                years > 0 -> "$years years, $remainingMonths months"
                else -> "$months months"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding = ChildrenListViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChildViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bind(children[position])
    }

    override fun getItemCount() = children.size

    fun updateChildren(newChildren: List<Child>) {
        children = newChildren
        notifyDataSetChanged()
    }
}