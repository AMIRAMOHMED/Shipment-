package com.example.hyperdesigntask.home.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hyperdesigntask.data.model.ApiResponse
import com.example.hyperdesigntask.data.model.RequestQuotation
import com.example.hyperdesigntask.data.repo.AuthRepo
import com.example.hyperdesigntask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RequestQuotationViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {
    private val _requestQuotationState = MutableStateFlow<Resource<ApiResponse>>(Resource.Loading)
    val requestQuotationState: StateFlow<Resource<ApiResponse>> get() = _requestQuotationState

    fun requestQuotation(request: RequestQuotation) {
        viewModelScope.launch {
            _requestQuotationState.value = Resource.Loading
            try {
                val response = authRepo.sendRequestQuotation(request)

                if (response.message == "success") {
                    _requestQuotationState.value = Resource.Success(response)
                } else {
                    _requestQuotationState.value = Resource.Error(response.message ?: "Unknown error occurred")
                }
            } catch (e: Exception) {
                _requestQuotationState.value = Resource.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
