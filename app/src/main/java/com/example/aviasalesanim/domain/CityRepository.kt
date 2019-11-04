package com.example.aviasalesanim.domain

import com.example.aviasalesanim.domain.model.City

interface CityRepository {

    suspend fun cities(query: String): List<City>
}