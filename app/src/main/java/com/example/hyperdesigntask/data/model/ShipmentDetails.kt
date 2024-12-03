package com.example.hyperdesigntask.data.model

data class ShipmentDetails(
    val id: Int,
    val shipment_name: String,
    val description: String,
    val comment: String,
    val status: String,
    val Containers: List<ContainerDetails>
)

data class ContainerDetails(
    val id: Int,
    val container_number: String,
    val size: String,
    val weight: String,
    val dimension: String
)
data class ShipmentDetailsResponse(
    val shippmentDetails: ShipmentDetails,
    val status_code: Int
)
data class ShipmentDetailsRequest(
    val id: String
)