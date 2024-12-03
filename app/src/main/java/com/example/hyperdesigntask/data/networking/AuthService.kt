package com.example.hyperdesigntask.data.networking
import com.example.hyperdesigntask.data.model.CountriesResponse
import com.example.hyperdesigntask.data.model.RefreshRequest
import com.example.hyperdesigntask.data.model.RefreshResponse
import com.example.hyperdesigntask.data.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthService {
    @POST("refresh")
    suspend fun refreshAccessToken(
        @Body request: RefreshRequest,
        @Header("Cookie") authHeader: String
    ): RefreshResponse
    @Multipart
    @POST("register")
    suspend fun registerUser(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("country_id") countryId: RequestBody,
        @Part("type") type: RequestBody,
        @Part file: MultipartBody.Part
    ): RegisterResponse

    @Multipart
    @POST("login")
    suspend fun loginUer(
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("token") token: RequestBody,
    ): RegisterResponse


    @POST("countries")
    suspend fun getCountries(): CountriesResponse

}