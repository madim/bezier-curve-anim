package com.example.aviasalesanim.data.api

import com.example.aviasalesanim.data.api.model.AutocompleteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HotelLookApi {

    @GET("autocomplete")
    suspend fun cities(
        @Query("term") query: String,
        @Query("lang") lang: String = "en"
    ): AutocompleteResponse
}