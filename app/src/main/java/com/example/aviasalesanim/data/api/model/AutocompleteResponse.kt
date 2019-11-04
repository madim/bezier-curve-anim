package com.example.aviasalesanim.data.api.model

import com.example.aviasalesanim.domain.model.City
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AutocompleteResponse(
    val cities: List<City>
)