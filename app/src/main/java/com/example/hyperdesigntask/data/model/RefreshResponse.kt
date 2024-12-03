package com.example.hyperdesigntask.data.model

data class RefreshResponse(
    val access_token: String?=null,
    val user: Userr?=null,
    val message: String?=null,
    val status: Int?=null
)

data class Userr(
    val id: Int?=null,
    val name: String?=null,
    val image: String?=null,
    val type: String?=null,
    val email: String?=null,
    val admin: String?=null,
    val active: String?=null,
    val phone: String?=null,
    val created_at: String?=null,
    val updated_at: String?=null
)