package com.akirachix.totosteps

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.akirachix.totosteps.databinding.ActivityAssessmentResourcesAllBinding

class AssessmentResourcesAllActivity : AppCompatActivity() {
    lateinit var binding: ActivityAssessmentResourcesAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentResourcesAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, Assessement_child_resource::class.java)
            startActivity(intent)

            finish()
        }, 2000)



        binding.rvChildren.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        displayResources()


    }

    fun displayResources() {
        val resource1 = resources(
            "Teach your child how to crawl and walk",
            "https://images.pexels.com/photos/1586558/pexels-photo-1586558.jpeg?auto=compress&cs=tinysrgb&w=400",
            listOf("Tip 1", "Tip 2"),
            listOf("Activity 1", "Activity 2")
        )
        val resource2 = resources(
            "Teach your child how to crawl and walk",
            "https://images.pexels.com/photos/18990374/pexels-photo-18990374/free-photo-of-smiling-child-with-curly-hair.jpeg?auto=compress&cs=tinysrgb&w=400&lazy=load",
            listOf("Tip 3", "Tip 4"),
            listOf("Activity 3", "Activity 4")
        )
        val resource3 = resources(
            "Teach your child how to crawl and walk",
            "https://images.pexels.com/photos/16091852/pexels-photo-16091852/free-photo-of-ethiopian-baby-pictures-by-john.jpeg?auto=compress&cs=tinysrgb&w=400",
            listOf("Tip 5", "Tip 6"),
            listOf("Activity 5", "Activity 6")
        )
        val resource4 = resources(
            "Teach your child how to crawl and walk",
            "https://images.pexels.com/photos/18990374/pexels-photo-18990374/free-photo-of-smiling-child-with-curly-hair.jpeg?auto=compress&cs=tinysrgb&w=400&lazy=load",
            listOf("Tip 7", "Tip 8"),
            listOf("Activity 7", "Activity 8")
        )
        val resource5 = resources(
            "Teach your child how to crawl and walk",
            "https://images.pexels.com/photos/18990374/pexels-photo-18990374/free-photo-of-smiling-child-with-curly-hair.jpeg?auto=compress&cs=tinysrgb&w=400&lazy=load",
            listOf("Tip 9", "Tip 10"),
            listOf("Activity 9", "Activity 10")
        )
        val resource6 = resources(
            "Teach your child how to crawl and walk",
            "https://images.pexels.com/photos/18990374/pexels-photo-18990374/free-photo-of-smiling-child-with-curly-hair.jpeg?auto=compress&cs=tinysrgb&w=400&lazy=load",
            listOf("Tip 11", "Tip 12"),
            listOf("Activity 11", "Activity 12")
        )
        val resource7 = resources(
            "Teach your child how to crawl and walk",
            "https://images.pexels.com/photos/18990374/pexels-photo-18990374/free-photo-of-smiling-child-with-curly-hair.jpeg?auto=compress&cs=tinysrgb&w=400&lazy=load",
            listOf("Tip 13", "Tip 14"),
            listOf("Activity 13", "Activity 14")
        )

        val resourcesList =
            listOf(resource1, resource2, resource3, resource4, resource5, resource6, resource7)

        val resourcesAdapter = ResourceAdapter(resourcesList)
        binding.rvChildren.adapter = resourcesAdapter
    }

}