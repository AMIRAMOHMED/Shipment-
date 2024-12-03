package com.example.hyperdesigntask.data.model

data class RequestQuotation(
    val shipmentName: String,
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
