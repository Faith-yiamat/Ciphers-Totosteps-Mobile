package com.akirachix.totosteps.models

import com.google.gson.annotations.SerializedName

data class Child(
    @SerializedName("child_id") val childId: Int,
    val username: String,
    @SerializedName("date_of_birth") val dateOfBirth: String,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val parent: Int
)
