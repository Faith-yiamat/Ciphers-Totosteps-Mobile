package com.akirachix.totosteps.models

import com.google.gson.annotations.SerializedName

data class ParentResponse(
    @SerializedName("user_id") val userId: Int,
    val children: List<Child>,
    @SerializedName("last_login") val lastLogin: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_active") val isActive: Boolean,
    val role: String,
    @SerializedName("user_permissions") val userPermissions: List<String>
)
