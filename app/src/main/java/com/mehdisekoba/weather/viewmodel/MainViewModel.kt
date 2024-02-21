package com.mehdisekoba.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehdisekoba.weather.data.model.ResponseCurrentLocation
import com.mehdisekoba.weather.data.model.ResponseCurrentWeather
import com.mehdisekoba.weather.data.model.ResponseForecast
import com.mehdisekoba.weather.data.repository.MainRepository
import com.mehdisekoba.weather.utils.network.NetworkRequest
import com.mehdisekoba.weather.utils.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Suppress("ktlint:standard:property-naming")
class MainViewModel
    @Inject
    constructor(private val repository: MainRepository) : ViewModel() {
        // Api
        // geocoding
        private val _geoWeatherData = MutableLiveData<NetworkRequest<ResponseCurrentLocation>>()
        val geoWeatherData: LiveData<NetworkRequest<ResponseCurrentLocation>> = _geoWeatherData

        fun callGeoWeatherApi(
            lat: Double,
            lon: Double,
        ) = viewModelScope.launch {
            _geoWeatherData.value = NetworkRequest.Loading()
            val response = repository.getGeocoding(lat, lon)
            _geoWeatherData.value = NetworkResponse(response).generateResponse()
        }

        // Current weather
        private val _currentWeatherData = MutableLiveData<NetworkRequest<ResponseCurrentWeather>>()
        val currentWeatherData: LiveData<NetworkRequest<ResponseCurrentWeather>> = _currentWeatherData

        fun callCurrentWeatherApi(
            lat: Double,
            lon: Double,
            lan: String,
        ) = viewModelScope.launch {
            _currentWeatherData.value = NetworkRequest.Loading()
            val response = repository.getCurrentWeather(lat, lon, lan)
            _currentWeatherData.value = NetworkResponse(response).generateResponse()
        }

        // Forecast
        private val _forecastData = MutableLiveData<NetworkRequest<ResponseForecast>>()
        val forecastData: LiveData<NetworkRequest<ResponseForecast>> = _forecastData

        fun callForecastApi(
            lat: Double,
            lon: Double,
            lan: String,
        ) = viewModelScope.launch {
            _forecastData.value = NetworkRequest.Loading()
            val response = repository.getForecast(lat, lon, lan)
            _forecastData.value = NetworkResponse(response).generateResponse()
        }
    }
