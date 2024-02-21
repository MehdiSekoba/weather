package com.mehdisekoba.weather.data.model

import com.google.gson.annotations.SerializedName

@Suppress("ktlint:standard:value-parameter-comment")
data class ResponseErrors(
    @SerializedName("cod")
    val cod: Int?, // 401
    @SerializedName("message")
    val message: String?, // Invalid API key. Please see https://openweathermap.org/faq#error401 for more info.
)
