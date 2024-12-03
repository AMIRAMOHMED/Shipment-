package com.example.hyperdesigntask.home.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hyperdesigntask.data.model.ShipmentDetails
import com.example.hyperdesigntask.data.repo.ShipmentRepo
import com.example.hyperdesigntask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipmentItemDetailsViewModel @Inject constructor(
    private val shipmentRepository: ShipmentRepo
) : ViewModel() {

    private val _shipmentDetailsState = MutableStateFlow<Resource<ShipmentDetails>>(Resource.Loading)
    val shipmentDetailsState: StateFlow<Resource<ShipmentDetails>> get() = _shipmentDetailsState

    fun getShipmentDetails(id: String) {
        viewModelScope.launch {
            _shipmentDetailsState.value = Resource.Loading
            try {
                val response = shipmentRepository.getShipmentDetails(id)

                if (response.status_code == 200) {
                    _shipmentDetailsState.value = Resource.Success(response.shippmentDetails)
                } else {
                    _shipmentDetailsState.value = Resource.Error("Failed to fetch shipment details")
                }
            } catch (e: Exception) {
                _shipmentDetailsState.value = Resource.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}