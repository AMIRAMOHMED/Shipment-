package com.example.hyperdesigntask.networking
import com.example.hyperdesigntask.data.model.ApiResponse
import com.example.hyperdesigntask.data.model.PageRequest
import com.example.hyperdesigntask.data.model.RefreshRequest
import com.example.hyperdesigntask.data.model.RegisterResponse
import com.example.hyperdesigntask.data.model.RequestQuotation
import com.example.hyperdesigntask.data.model.ShippmentsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthService {
    @POST("requestQuotation")
    suspend fun sendRequestQuotation(@Body request: RequestQuotation): ApiResponse
    @POST("refresh")

    suspend fun refreshToken(
        @Body requestBody: RefreshRequest
    ): RegisterResponse

    @Multipart
    @POST("register")
    suspend fun registerUser(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("country_id") countryId: RequestBody,
        @Part("type") type: RequestBody,
        @Part file:MultipartBody.Part
    ): RegisterResponse

    @Multipart
    @POST("login")
    suspend fun loginUer(
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("token") token: RequestBody,
    ): RegisterResponse


    @POST("getShippments")
    suspend fun getShippments(@Body request: PageRequest): ShippmentsResponse
}
