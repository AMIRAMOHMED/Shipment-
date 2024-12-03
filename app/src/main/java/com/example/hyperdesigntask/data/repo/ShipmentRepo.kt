package com.example.hyperdesigntask.data.repo

import com.example.hyperdesigntask.data.model.*
import com.example.hyperdesigntask.data.networking.ShipmentService
import javax.inject.Inject

class ShipmentRepo @Inject constructor(

    private val api: ShipmentService

)  {

     suspend fun sendRequestQuotation(request: RequestQuotation): ApiResponse {
        return api.sendRequestQuotation(request)
    }

     suspend fun getShipments(page: String): ShippmentsResponse {
        val request = PageRequest(page)
        return api.getShippments(request)
    }

     suspend fun getShipmentDetails(id: String): ShipmentDetailsResponse {
        val request = ShipmentDetailsRequest(id)
        return api.getShipmentDetails(request)
    }
}