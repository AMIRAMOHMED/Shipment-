package com.example.hyperdesigntask.data.repo
import android.annotation.SuppressLint
import com.example.hyperdesigntask.data.local.TokenManager
import com.example.hyperdesigntask.data.model.CountriesResponse
import com.example.hyperdesigntask.data.model.LoginRequest
import com.example.hyperdesigntask.data.model.RefreshRequest
import com.example.hyperdesigntask.data.model.RegisterRequest
import com.example.hyperdesigntask.data.model.RegisterResponse
import com.example.hyperdesigntask.data.networking.AuthService
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
    suspend fun refreshToken() {
        try {
            val header =  mapOf(
                "Authorization" to "Bearer YOUR_TOKEN",
                "Content-Type" to "application/json"
            )
            val response = api.refreshAccessToken(RefreshRequest("1",),
                "eyJpdiI6IkFvQnlVZG5CMjNsQ2RQZGFyeUxwMmc9PSIsInZhbHVlIjoiQUZFR1wvbnQwdEM5azFSWGhqNE0wY1FoUXRCM3pjcXhIXC9XNGNjMFV4REJEZG1hdmJvVnBBaDAyWXh0SVNEZWhoRUQ0QkM2T1JLU2xIVzREMmsycGg3Zz09IiwibWFjIjoiNmRjMTg4OWQ2ZDRmODkxZWNmYTRlNWVjYzVjNDhlNzJjYWFmNjBkMDI4NzI4ZjE0MTQ5Y2M3ZmUzOWM2YjgzMSJ9"


                )
            tokenManger.saveAccessToken(response.access_token.orEmpty())
        } catch (e: HttpException) {
            if (e.code() == 500) {

            } else {

            }
        } catch (e: Exception) {

        }
    }

    suspend fun loginUer(request: LoginRequest): RegisterResponse {
        return api.loginUer(
            phone = request.phone.toRequestBody(),
            password = request.password.toRequestBody(),
            token = request.token.toRequestBody(),
            )
    }

    suspend fun getCountries(): CountriesResponse {
        return api.getCountries()
    }

}
