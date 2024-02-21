package com.mehdisekoba.weather.data.model

import com.google.gson.annotations.SerializedName

@Suppress("ktlint:standard:value-parameter-comment")
data class ResponsePollution(
    @SerializedName("coord")
    val coord: Coord?,
    @SerializedName("list")
    val list: List<Data>?,
) {
    data class Coord(
        @SerializedName("lat")
        val lat: Double?, // 44.34
        @SerializedName("lon")
        val lon: Double?, // 10.99
    )

    data class Data(
        @SerializedName("components")
        val components: Components,
        @SerializedName("dt")
        val dt: Int?, // 1702654264
        @SerializedName("main")
        val main: Main,
    ) {
        data class Components(
            @SerializedName("co")
            val co: Double?, // 694.28
            @SerializedName("nh3")
            val nh3: Double?, // 6.71
            @SerializedName("no")
            val no: Double?, // 5.42
            @SerializedName("no2")
            val no2: Double?, // 31.19
            @SerializedName("o3")
            val o3: Double?, // 1.35
            @SerializedName("pm10")
            val pm10: Double?, // 29.27
            @SerializedName("pm2_5")
            val pm25: Double?, // 27.26
            @SerializedName("so2")
            val so2: Double?, // 0.84
        )

        data class Main(
            @SerializedName("aqi")
            val aqi: Int?, // 3
        )
    }
}
