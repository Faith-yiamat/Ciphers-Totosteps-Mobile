package com.akirachix.totosteps.models

data class ChildData(
    val username: String,
    val date_of_birth: String,
    val is_active: Boolean,
    val parent: Int
)