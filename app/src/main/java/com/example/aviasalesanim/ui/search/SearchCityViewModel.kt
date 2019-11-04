package com.example.aviasalesanim.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aviasalesanim.combineAndCompute
import com.example.aviasalesanim.domain.CityRepository
import com.example.aviasalesanim.domain.model.City
import com.example.aviasalesanim.domain.model.Location
import com.example.aviasalesanim.ui.model.Event
import com.example.aviasalesanim.ui.model.Nav
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SearchCityViewModel(
    private val cityRepository: CityRepository,
    private val ioContext: CoroutineContext = Dispatchers.IO
) : ViewModel() {

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> get() = _cities

    private val _departureCity = MutableLiveData<City>()
    val departureCity: LiveData<City> get() = _departureCity

    private val _destinationCity = MutableLiveData<City>()
    val destinationCity: LiveData<City> get() = _destinationCity

    private val _nav = MutableLiveData<Event<Nav>>()
    val nav: LiveData<Event<Nav>> get() = _nav

    val departureAndDestination: LiveData<Pair<Location, Location>>

    init {
        departureAndDestination = _departureCity.combineAndCompute(_destinationCity) { a, b ->
            Pair(a.location, b.location)
        }
    }

    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            val cities = withContext(ioContext) { cityRepository.cities(query) }
            _cities.value = cities
        }
    }

    fun selectCity(city: City) {
        when (_nav.value?.peekContent()) {
            Nav.Departure -> _departureCity.value = city
            Nav.Destination -> _destinationCity.value = city
            else -> return
        }
        _cities.value = null
    }

    fun navigateTo(nav: Nav) {
        _nav.value = Event(nav)
    }
}
