package com.akirachix.totosteps

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akirachix.totosteps.models.Milestone

class MilestoneAdapter(
    private val milestones: List<Milestone>,
    private val onClick: (Milestone) -> Unit // This will be called when an item is clicked
) : RecyclerView.Adapter<MilestoneAdapter.MilestoneViewHolder>() {

    inner class MilestoneViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text_view)
        val ageTextView: TextView = view.findViewById(R.id.age)
        val categoryTextView: TextView = view.findViewById(R.id.category)

        fun bind(milestone: Milestone) {
            nameTextView.text = milestone.name
            ageTextView.text = "Age: ${milestone.age}"
            categoryTextView.text = "Category: ${milestone.category}"

            // Set the click listener with logging
            itemView.setOnClickListener {
                Log.d("MilestoneAdapter", "Clicked: ${milestone.name}")
                onClick(milestone)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_milestone_adapter, parent, false)
        return MilestoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: MilestoneViewHolder, position: Int) {
        holder.bind(milestones[position])
    }

    override fun getItemCount(): Int = milestones.size
}
