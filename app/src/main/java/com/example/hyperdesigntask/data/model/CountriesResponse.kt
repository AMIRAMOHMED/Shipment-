package com.example.hyperdesigntask.data.model

data class CountriesResponse(
    val countries: List<Country>,
    val status_code: Int
)

data class Country(
    val id: Int,
    val name: String,
    val code: String?
)