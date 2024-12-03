package com.example.hyperdesigntask.data.repo
import android.annotation.SuppressLint
import android.util.Log
import com.example.hyperdesigntask.data.local.TokenManager
import com.example.hyperdesigntask.data.model.ApiResponse
import com.example.hyperdesigntask.data.model.LoginRequest
import com.example.hyperdesigntask.data.model.PageRequest
import com.example.hyperdesigntask.data.model.RefreshRequest
import com.example.hyperdesigntask.data.model.RegisterRequest
import com.example.hyperdesigntask.data.model.RegisterResponse
import com.example.hyperdesigntask.data.model.RequestQuotation
import com.example.hyperdesigntask.data.model.ShipmentDetailsRequest
import com.example.hyperdesigntask.data.model.ShipmentDetailsResponse
import com.example.hyperdesigntask.data.model.ShippmentsResponse
import com.example.hyperdesigntask.networking.AuthService
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
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
    suspend fun refreshToken(id: String) {
        try {
            Log.i("refresh", "AuthRepo - Refresh Token API Called with ID: $id")
            val response = api.refreshAccessToken(RefreshRequest(id))
            Log.i("refresh", "AuthRepo - Response Received: $response")
            tokenManger.saveAccessToken(response.access_token)
            Log.i("refresh", "AuthRepo - New Token Saved")
        } catch (e: HttpException) {
            if (e.code() == 500) {
                Log.e("refresh", "Server Error: ${e.response()?.errorBody()?.string()}")
            } else {
                Log.e("refresh", "HTTP Error: ${e.code()} - ${e.message()}")
            }
        } catch (e: Exception) {
            Log.e("refresh", "Unexpected Error: ${e.message}")
        }
    }

    suspend fun loginUer(request: LoginRequest): RegisterResponse {
        return api.loginUer(
            phone = request.phone.toRequestBody(),
            password = request.password.toRequestBody(),
            token = request.token.toRequestBody(),
            )


    }
    suspend fun  sendRequestQuotation(request: RequestQuotation): ApiResponse
    {
        return  api.sendRequestQuotation(
            request
        )
    }
    suspend fun getShippments(page: String): ShippmentsResponse {
        val request = PageRequest(page)
        return api.getShippments(request)
    }
    suspend fun getShipmentDetails(id: String): ShipmentDetailsResponse {
        val request = ShipmentDetailsRequest(id)
        return api.getShipmentDetails(request)
    }
}
