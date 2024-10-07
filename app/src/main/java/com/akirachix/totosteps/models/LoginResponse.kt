package com.akirachix.totosteps.models

data class LoginResponse(
    val status: String,
    val message: String,
    val user: User
)

data class User(
    val user_id: Int,
    val first_name: String,
    val last_name: String,
    val email: String
)
