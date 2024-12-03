package com.example.hyperdesigntask.data.model

data class ShippmentsResponse(
    val message: String,
    val shippments: ShippmentsData
)

data class ShippmentsData(
    val total: Int,
    val per_page: Int,
    val current_page: Int,
    val last_page: Int,
    val next_page_url: String?,
    val prev_page_url: String?,
    val from: Int,
    val to: Int,
    val data: List<Shipment>
)

data class Shipment(
    val id: Int,
    val shipment_name: String,
    val description: String,
    val comment: String,
    val status: String,
    val Containers: List<Container>
)

