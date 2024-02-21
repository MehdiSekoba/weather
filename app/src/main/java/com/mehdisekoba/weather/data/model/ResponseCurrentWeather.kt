package com.mehdisekoba.weather.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Suppress("ktlint:standard:value-parameter-comment")
data class ResponseCurrentWeather(
    @SerializedName("base")
    val base: String?, // stations
    @SerializedName("clouds")
    val clouds: @RawValue Clouds?,
    @SerializedName("cod")
    val cod: Int?, // 200
    @SerializedName("coord")
    val coord: @RawValue Coord?,
    @SerializedName("dt")
    val dt: Int?, // 1702460981
    @SerializedName("id")
    val id: Int?, // 3163858
    @SerializedName("main")
    val main: @RawValue Main?,
    @SerializedName("name")
    val name: String?, // Zocca
    @SerializedName("rain")
    val rain: @RawValue Rain?,
    @SerializedName("sys")
    val sys: @RawValue Sys?,
    @SerializedName("timezone")
    val timezone: Int?, // 3600
    @SerializedName("visibility")
    val visibility: Int?, // 10000
    @SerializedName("weather")
    val weather: @RawValue List<Weather?>?,
    @SerializedName("wind")
    val wind: @RawValue Wind?,
) : Parcelable {
    data class Clouds(
        @SerializedName("all")
        val all: Int?, // 100
    )

    data class Coord(
        @SerializedName("lat")
        val lat: Double?, // 44.34
        @SerializedName("lon")
        val lon: Double?, // 10.99
    )

    data class Main(
        @SerializedName("feels_like")
        val feelsLike: Double?, // 3.88
        @SerializedName("grnd_level")
        val grndLevel: Int?, // 915
        @SerializedName("humidity")
        val humidity: Int?, // 98
        @SerializedName("pressure")
        val pressure: Int?, // 1001
        @SerializedName("sea_level")
        val seaLevel: Int?, // 1001
        @SerializedName("temp")
        val temp: Double?, // 3.88
        @SerializedName("temp_max")
        val tempMax: Double?, // 10.73
        @SerializedName("temp_min")
        val tempMin: Double?, // 1.49
    )

    data class Rain(
        @SerializedName("1h")
        val h: Double?, // 2.05
    )

    data class Sys(
        @SerializedName("country")
        val country: String?, // IT
        @SerializedName("id")
        val id: Int?, // 2004688
        @SerializedName("sunrise")
        val sunrise: Int?, // 1702449795
        @SerializedName("sunset")
        val sunset: Int?, // 1702481819
        @SerializedName("type")
        val type: Int?, // 2
    )

    data class Weather(
        @SerializedName("description")
        val description: String?, // بارش باران
        @SerializedName("icon")
        val icon: String?, // 10d
        @SerializedName("id")
        val id: Int?, // 501
        @SerializedName("main")
        val main: String?, // Rain
    )

    data class Wind(
        @SerializedName("deg")
        val deg: Int?, // 214
        @SerializedName("gust")
        val gust: Double?, // 5.57
        @SerializedName("speed")
        val speed: Double?, // 1.18
    )
}
