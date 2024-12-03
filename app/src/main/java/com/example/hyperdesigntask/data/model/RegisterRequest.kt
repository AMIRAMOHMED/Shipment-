package com.example.hyperdesigntask.data.model

import okhttp3.MultipartBody

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val country_id: String,
    val type: String,
    val file: MultipartBody.Part

)
