package com.example.hyperdesigntask.auth.viewmodel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hyperdesigntask.data.local.TokenManager
import com.example.hyperdesigntask.data.model.RegisterRequest
import com.example.hyperdesigntask.data.model.RegisterResponse
import com.example.hyperdesigntask.data.repo.AuthRepo
import com.example.hyperdesigntask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private  val tokenManager: TokenManager,
) : ViewModel() {
    private val _registerState = MutableStateFlow<Resource<RegisterResponse>>(Resource.Loading)
    val registerState: StateFlow<Resource<RegisterResponse>> get() = _registerState
    private val _countriesState = MutableStateFlow<Resource<List<String>>>(Resource.Loading)
    val countriesState: StateFlow<Resource<List<String>>> = _countriesState

    fun refreshToken() {
        viewModelScope.launch {
            try {
                val newToken = authRepo.refreshToken()
                Log.d("refresh", "New access token: $newToken")
            } catch (e: Exception) {
                Log.e("refresh", "Failed to refresh token: ${e.message}")
            }
        }
    }

    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading
            try {
                val response = authRepo.registerUser(request)

                // Check the response for validity
                if (response.access_token.isNullOrEmpty() && response.user == null) {
                    _registerState.value = Resource.Error(response.message ?: "Unknown error")
                } else {
                    tokenManager.saveUserId(response.user?.id.toString())
                    tokenManager.saveAccessToken(response.access_token.toString())
                    _registerState.value = Resource.Success(response)
                }
            } catch (e: HttpException) {
                _registerState.value = Resource.Error(e.message ?: "An error occurred", e.code())
            } catch (e: Exception) {
                _registerState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchCountries() {
        viewModelScope.launch {
            try {
                val response = authRepo.getCountries()
                val countriesList = response.countries.map { it.name }
                _countriesState.value = Resource.Success(countriesList)
            } catch (e: Exception) {
                _countriesState.value = Resource.Error("Failed to fetch countries")
            }
        }
    }



}