package com.example.aviasalesanim.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class City(
    val fullname: String,
    val location: Location
)

@JsonClass(generateAdapter = true)
data class Location(
    val lat: Double,
    val lon: Double
)