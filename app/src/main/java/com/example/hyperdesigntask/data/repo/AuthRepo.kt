package com.example.hyperdesigntask.data.repo
import android.annotation.SuppressLint
import com.example.hyperdesigntask.data.local.TokenManager
import com.example.hyperdesigntask.data.model.RefreshRequest
import com.example.hyperdesigntask.data.model.RegisterRequest
import com.example.hyperdesigntask.data.model.RegisterResponse
import com.example.hyperdesigntask.networking.AuthService
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val api: AuthService,
    private  val tokenManger:TokenManager
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

    @SuppressLint("SuspiciousIndentation")
    suspend fun  refreshToken():RegisterResponse{
        val userId = tokenManger.getUserId()
        userId?.let {
         val request =   RefreshRequest(
             id = userId
         )
           return  api.refreshToken(request)
        } ?: throw IllegalStateException("User ID is null")
    }
}
