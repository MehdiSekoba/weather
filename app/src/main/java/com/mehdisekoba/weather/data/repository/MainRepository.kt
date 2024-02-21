@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.mehdisekoba.weather.data.repository

import com.mehdisekoba.weather.data.network.ApiServices
import com.mehdisekoba.weather.utils.*
import javax.inject.Inject

class MainRepository
    @Inject
    constructor(private val api: ApiServices) {
        // Database

        // Api
        suspend fun getGeocoding(
            lat: Double,
            lon: Double,
        ) = api.getGeocoding(lat, lon)

        suspend fun getCurrentWeather(
            lat: Double,
            lon: Double,
            lan: String,
        ) = api.getCurrentWeather(lat, lon, lan, METRIC)

        suspend fun getForecast(
            lat: Double,
            lon: Double,
            lan: String,
        ) = api.getForecast(lat, lon, lan, METRIC)
    }
