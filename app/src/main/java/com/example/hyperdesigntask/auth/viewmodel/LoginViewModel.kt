package com.example.hyperdesigntask.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hyperdesigntask.data.local.TokenManager
import com.example.hyperdesigntask.data.model.LoginRequest
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
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private  val tokenManager: TokenManager,
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<RegisterResponse>>(Resource.Loading)
    val loginState: StateFlow<Resource<RegisterResponse>> get() = _loginState

    fun loginUer(request: LoginRequest) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            try {
                val response = authRepo.loginUer(request)

                // Check the response for validity
                if (response.access_token.isNullOrEmpty() && response.user == null) {
                    _loginState.value = Resource.Error("cannot login , please try again")
                } else {
                    tokenManager.saveUserId(response.user?.id.toString())
                    tokenManager.saveAccessToken(response.access_token.toString())
                    _loginState.value = Resource.Success(response)
                }
            } catch (e: HttpException) {
                _loginState.value = Resource.Error(e.message ?: "An error occurred", e.code())
            } catch (e: Exception) {
                _loginState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
    fun getUserId(): String? {
        return tokenManager.getUserId()
    }

}
