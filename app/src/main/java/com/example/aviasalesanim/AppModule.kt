package com.example.aviasalesanim

import  com.example.aviasalesanim.data.api.HotelLookApi
import com.example.aviasalesanim.data.DefaultCityRepository
import com.example.aviasalesanim.domain.CityRepository
import com.example.aviasalesanim.ui.search.SearchCityViewModel
import com.squareup.moshi.Moshi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    single {
        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://yasen.hotellook.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        retrofit.create(HotelLookApi::class.java) as HotelLookApi
    }

    single {
        DefaultCityRepository(
            hotelLookApi = get()
        ) as CityRepository
    }

    viewModel {
        SearchCityViewModel(
            cityRepository = get()
        )
    }
}