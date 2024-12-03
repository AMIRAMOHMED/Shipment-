package com.example.hyperdesigntask.home.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hyperdesigntask.data.model.ShippmentsResponse
import com.example.hyperdesigntask.data.repo.ShipmentRepo
import com.example.hyperdesigntask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val shipmentRepository: ShipmentRepo
) : ViewModel() {

    private val _shippments = MutableStateFlow<Resource<ShippmentsResponse>>(Resource.Loading)
    val shippments  = _shippments.asStateFlow()

        fun fetchShippments(page: String) {
            viewModelScope.launch {
                _shippments.value = Resource.Loading
                try {
                    val response = shipmentRepository.getShipments(page)
                    _shippments.value = Resource.Success(response)
                } catch (e: Exception) {
                    _shippments.value = Resource.Error("Error fetching shipments: ${e.localizedMessage}")
                }
            }
        }

}