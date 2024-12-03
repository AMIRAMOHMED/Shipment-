package com.example.hyperdesigntask.data.model

data class RefreshResponse(
    val access_token: String,
    val user: Userr,
    val message: String,
    val status: Int
)

data class Userr(
    val id: Int,
    val name: String,
    val image: String?,
    val type: String,
    val email: String,
    val admin: String,
    val active: String,
    val phone: String?,
    val created_at: String,
    val updated_at: String
)