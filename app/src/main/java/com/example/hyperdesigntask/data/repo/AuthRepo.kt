package com.example.hyperdesigntask.data.repo
import android.util.Log
import com.example.hyperdesigntask.data.model.RegisterRequest
import com.example.hyperdesigntask.data.model.RegisterResponse
import com.example.hyperdesigntask.networking.HyperDesginTaskService
import com.example.hyperdesigntask.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val api: HyperDesginTaskService
) {
    suspend fun registerUser(request: RegisterRequest): Resource<RegisterResponse> {
        return try {
            val response = withContext(Dispatchers.IO) {
                api.registerUser(
                    name = request.name.toRequestBody(),
                    email = request.email.toRequestBody(),
                    phone = request.phone.toRequestBody(),
                    password = request.password.toRequestBody(),
                    countryId = request.country_id.toRequestBody(),
                    type = request.type.toRequestBody(),
                    file = request.file
                )
            }
            Log.d("AuthRepo", "Registration successful: ${response.message}")

            Resource.Success(response)

        } catch (e: HttpException) {
            Log.e("AuthRepo", "HTTP error: ${e.message}")

            Resource.Error(e.message ?: "An error occurred", e.code())
        } catch (e: Exception) {
            Log.e("AuthRepo", "Unknown error: ${e.message}")

            Resource.Error(e.message ?: "Unknown error")
        }
    }
}
