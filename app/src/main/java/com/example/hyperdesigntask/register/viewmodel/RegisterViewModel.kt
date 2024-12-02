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
            _registerState.value = result
        }
    }
}