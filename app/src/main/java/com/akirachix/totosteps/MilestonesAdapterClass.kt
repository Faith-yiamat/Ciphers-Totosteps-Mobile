//package com.akirachix.totosteps
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.akirachix.totosteps.databinding.ChildrenListViewBinding
//import com.akirachix.totosteps.databinding.ChildrenMilestoneListBinding
//
//class MilestonesAdapterClass(var childMilestones: List<ChildrenDataClass>):RecyclerView.Adapter<MilestonsViewHolder>(){
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestonsViewHolder {
//
//        var binding = ChildrenMilestoneListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        return MilestonsViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MilestonsViewHolder, position: Int) {
//        var milsestonesChildren = childMilestones[position]
//        holder.binding.tvName.text = milsestonesChildren.name
//        holder.binding.tvAge.text = milsestonesChildren.age    }
//
//    override fun getItemCount(): Int {
//        return childMilestones.size
//    }
//}
//
//class MilestonsViewHolder(var binding: ChildrenMilestoneListBinding):RecyclerView.ViewHolder(binding.root){
//
//}