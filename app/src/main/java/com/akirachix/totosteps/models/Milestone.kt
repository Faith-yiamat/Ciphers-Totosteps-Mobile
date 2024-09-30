package com.akirachix.totosteps.models

data class Milestone(
    val milestone_id: Int,
    val name: String,
    val age: Int,
    val category: String,
    val summary: List<String>,
    val created_at: String,
    val updated_at: String,
    val is_current: Boolean,
    val child_id: Int?
)
