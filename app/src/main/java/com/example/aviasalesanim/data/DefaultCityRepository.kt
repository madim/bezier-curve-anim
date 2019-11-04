package com.example.aviasalesanim.data

import com.example.aviasalesanim.data.api.HotelLookApi
import com.example.aviasalesanim.domain.model.City
import com.example.aviasalesanim.domain.CityRepository

class DefaultCityRepository(
    private val hotelLookApi: HotelLookApi
) : CityRepository {

    override suspend fun cities(query: String): List<City> {
        val response = hotelLookApi.cities(query)

        return response.cities
    }
}