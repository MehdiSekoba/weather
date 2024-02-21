package com.mehdisekoba.weather.data.model

import com.google.gson.annotations.SerializedName

@Suppress("ktlint:standard:value-parameter-comment")
data class ResponseForecast(
    @SerializedName("city")
    val city: City?,
    @SerializedName("cnt")
    val cnt: Int?, // 40
    @SerializedName("cod")
    val cod: String?, // 200
    @SerializedName("list")
    val list: List<Data>,
    @SerializedName("message")
    val message: Int?, // 0
) {
    data class City(
        @SerializedName("coord")
        val coord: Coord?,
        @SerializedName("country")
        val country: String?, // IT
        @SerializedName("id")
        val id: Int?, // 3163858
        @SerializedName("name")
        val name: String?, // Zocca
        @SerializedName("population")
        val population: Int?, // 4593
        @SerializedName("sunrise")
        val sunrise: Int?, // 1702449795
        @SerializedName("sunset")
        val sunset: Int?, // 1702481819
        @SerializedName("timezone")
        val timezone: Int?, // 3600
    ) {
        data class Coord(
            @SerializedName("lat")
            val lat: Double?, // 44.34
            @SerializedName("lon")
            val lon: Double?, // 10.99
        )
    }

    data class Data(
        @SerializedName("clouds")
        val clouds: Clouds?,
        @SerializedName("dt")
        val dt: Int?, // 1702479600
        @SerializedName("dt_txt")
        val dtTxt: String?, // 2023-12-13 15:00:00
        @SerializedName("main")
        val main: Main?,
        @SerializedName("pop")
        val pop: Double?, // 0.37
        @SerializedName("sys")
        val sys: Sys?,
        @SerializedName("visibility")
        val visibility: Int?, // 10000
        @SerializedName("weather")
        val weather: List<Weather?>?,
        @SerializedName("wind")
        val wind: Wind?,
    ) {
        data class Clouds(
            @SerializedName("all")
            val all: Int?, // 90
        )

        data class Main(
            @SerializedName("feels_like")
            val feelsLike: Double?, // 5.4
            @SerializedName("grnd_level")
            val grndLevel: Int?, // 913
            @SerializedName("humidity")
            val humidity: Int?, // 85
            @SerializedName("pressure")
            val pressure: Int?, // 999
            @SerializedName("sea_level")
            val seaLevel: Int?, // 999
            @SerializedName("temp")
            val temp: Double?, // 6.8
            @SerializedName("temp_kf")
            val tempKf: Double?, // -0.1
            @SerializedName("temp_max")
            val tempMax: Double?, // 6.9
            @SerializedName("temp_min")
            val tempMin: Double?, // 6.8
        )

        data class Sys(
            @SerializedName("pod")
            val pod: String?, // d
        )

        data class Weather(
            @SerializedName("description")
            val description: String?, // پوشیده از ابر
            @SerializedName("icon")
            val icon: String?, // 04d
            @SerializedName("id")
            val id: Int?, // 804
            @SerializedName("main")
            val main: String?, // Clouds
        )

        data class Wind(
            @SerializedName("deg")
            val deg: Int?, // 221
            @SerializedName("gust")
            val gust: Double?, // 8.89
            @SerializedName("speed")
            val speed: Double?, // 2.07
        )
    }
}
