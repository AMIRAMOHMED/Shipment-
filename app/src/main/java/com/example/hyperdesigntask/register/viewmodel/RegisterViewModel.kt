package com.example.hyperdesigntask.register.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hyperdesigntask.data.model.RegisterRequest
import com.example.hyperdesigntask.data.model.RegisterResponse
import com.example.hyperdesigntask.data.repo.AuthRepo
import com.example.hyperdesigntask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _registerState = MutableStateFlow<Resource<RegisterResponse>>(Resource.Loading)
    val registerState: StateFlow<Resource<RegisterResponse>> get() = _registerState

    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading
            val result = authRepo.registerUser(request)

            // Handle response distinction here
            if (result is Resource.Success) {
                val response = result.data
                if (response.access_token.isNullOrEmpty() && response.user == null) {
                    _registerState.value = Resource.Error(response.message ?: "Unknown error")
                } else {
                    _registerState.value = Resource.Success(response)
                }
            } else {
                _registerState.value = result
            }
        }
    }

}