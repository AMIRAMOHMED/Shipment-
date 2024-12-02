package com.example.hyperdesigntask.data.repo
import com.example.hyperdesigntask.data.model.RegisterRequest
import com.example.hyperdesigntask.data.model.RegisterResponse
import com.example.hyperdesigntask.networking.AuthService
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val api: AuthService
) {
    suspend fun registerUser(request: RegisterRequest): RegisterResponse {
        return api.registerUser(
            name = request.name.toRequestBody(),
            email = request.email.toRequestBody(),
            phone = request.phone.toRequestBody(),
            password = request.password.toRequestBody(),
            countryId = request.country_id.toRequestBody(),
            type = request.type.toRequestBody(),
            file = request.file
        )
    }
}
