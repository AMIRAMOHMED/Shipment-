package com.example.hyperdesigntask.data.model

import com.google.gson.annotations.SerializedName
data class RequestQuotation(
    @SerializedName("shipment_name") val shipmentName: String,
    val description: String,
    val quantity: String,
    val containerNumber: List<Container>,
    val comment: String
)

data class Container(
    val number: String,
    val size: String,
    val weight: String,
    val dimension: String
)
