package com.mehdisekoba.weather.data.network

import com.mehdisekoba.weather.data.model.ResponseCurrentLocation
import com.mehdisekoba.weather.data.model.ResponseCurrentWeather
import com.mehdisekoba.weather.data.model.ResponseForecast
import com.mehdisekoba.weather.data.model.ResponsePollution
import com.mehdisekoba.weather.data.model.ResponseSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("geo/1.0/reverse")
    suspend fun getGeocoding(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Response<ResponseCurrentLocation>

    // The correct and principled solution is to use QueryMap
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String,
        @Query("units") units: String,
    ): Response<ResponseCurrentWeather>

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String,
        @Query("units") units: String,
    ): Response<ResponseForecast>

    // The correct and principled solution is to use QueryMap
    @GET("data/2.5/weather")
    suspend fun getCitiesList(
        @Query("q") q: String,
        @Query("units") units: String,
    ): Response<ResponseSearch>

    @GET("data/2.5/air_pollution")
    suspend fun getPollution(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Response<ResponsePollution>
}
