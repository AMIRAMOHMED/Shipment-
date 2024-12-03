package com.example.hyperdesigntask.data.networking


import com.example.hyperdesigntask.data.model.*
import retrofit2.http.Body
import retrofit2.http.POST

interface ShipmentService {

    @POST("requestQuotation")
    suspend fun sendRequestQuotation(@Body request: RequestQuotation): ApiResponse

    @POST("getShippments")
    suspend fun getShippments(@Body request: PageRequest): ShippmentsResponse

    @POST("shippment-details")
    suspend fun getShipmentDetails(@Body request: ShipmentDetailsRequest): ShipmentDetailsResponse
}