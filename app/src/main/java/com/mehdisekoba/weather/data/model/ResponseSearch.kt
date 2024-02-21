package com.mehdisekoba.weather.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Suppress("ktlint:standard:value-parameter-comment")
@Parcelize
data class ResponseSearch(
    @SerializedName("base")
    val base: String?, // stations
    @SerializedName("clouds")
    val clouds: @RawValue Clouds?,
    @SerializedName("cod")
    val cod: Int?, // 200
    @SerializedName("coord")
    val coord: @RawValue Coord?,
    @SerializedName("dt")
    val dt: Int?, // 1707928731
    @SerializedName("id")
    val id: Int?, // 112931
    @SerializedName("main")
    val main: @RawValue Main?,
    @SerializedName("name")
    val name: String?, // Tehran
    @SerializedName("sys")
    val sys: @RawValue Sys?,
    @SerializedName("timezone")
    val timezone: Int?, // 12600
    @SerializedName("visibility")
    val visibility: Int?, // 5000
    @SerializedName("weather")
    val weather: @RawValue List<Weather?>?,
    @SerializedName("wind")
    val wind: @RawValue Wind?,
) : Parcelable {
    data class Clouds(
        @SerializedName("all")
        val all: Int?, // 40
    )

    data class Coord(
        @SerializedName("lat")
        val lat: Double?, // 35.6944
        @SerializedName("lon")
        val lon: Double?, // 51.4215
    )

    data class Main(
        @SerializedName("feels_like")
        val feelsLike: Double?, // 287.04
        @SerializedName("humidity")
        val humidity: Int?, // 23
        @SerializedName("pressure")
        val pressure: Int?, // 1018
        @SerializedName("temp")
        val temp: Double?, // 288.82
        @SerializedName("temp_max")
        val tempMax: Double?, // 289.14
        @SerializedName("temp_min")
        val tempMin: Double?, // 286.94
    )

    data class Sys(
        @SerializedName("country")
        val country: String?, // IR
        @SerializedName("id")
        val id: Int?, // 47737
        @SerializedName("sunrise")
        val sunrise: Int?, // 1707881001
        @SerializedName("sunset")
        val sunset: Int?, // 1707920034
        @SerializedName("type")
        val type: Int?, // 2
    )

    data class Weather(
        @SerializedName("description")
        val description: String?, // haze
        @SerializedName("icon")
        val icon: String?, // 50n
        @SerializedName("id")
        val id: Int?, // 721
        @SerializedName("main")
        val main: String?, // Haze
    )

    data class Wind(
        @SerializedName("deg")
        val deg: Int?, // 120
        @SerializedName("speed")
        val speed: Double?, // 2.06
    )
}
