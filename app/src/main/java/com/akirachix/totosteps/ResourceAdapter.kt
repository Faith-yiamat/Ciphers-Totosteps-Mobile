package com.akirachix.totosteps

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akirachix.totosteps.activity.ResourceDetailsActivity
import com.akirachix.totosteps.databinding.ActivityResourceListBinding
import com.akirachix.totosteps.databinding.ChildrenListViewBinding
import com.akirachix.totosteps.databinding.ChildrenMilestoneListBinding
import com.google.gson.Gson

class ResourceAdapter(var resources: List<resources>):RecyclerView.Adapter<ResourcesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourcesViewHolder {
        var binding = ActivityResourceListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ResourcesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResourcesViewHolder, position: Int) {
        val resource = resources[position]
        holder.binding.tvAge.text = resource.title

        val gson = Gson()
        val tipsJson = gson.toJson(resource.tips)
        val activitiesJson = gson.toJson(resource.activities)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ResourceDetailsActivity::class.java)
            intent.putExtra("RESOURCE_TITLE", resource.title)
            intent.putExtra("RESOURCE_TIPS",tipsJson)
            intent.putExtra("RESOURCE_ACTIVITIES",activitiesJson)
            holder.itemView.context.startActivity(intent)
        }

    }



    override fun getItemCount(): Int {
        return resources.size
    }
    fun updateResources(newResources:List<resources>){
        resources = newResources
        notifyDataSetChanged()
    }
}

class ResourcesViewHolder(var binding: ActivityResourceListBinding):RecyclerView.ViewHolder(binding.root){

}

