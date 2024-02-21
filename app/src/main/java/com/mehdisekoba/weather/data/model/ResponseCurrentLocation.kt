package com.mehdisekoba.weather.data.model

import com.google.gson.annotations.SerializedName

class ResponseCurrentLocation : ArrayList<ResponseCurrentLocation.ResponseCurrentLocationItem>() {
    data class ResponseCurrentLocationItem(
        @SerializedName("country")
        val country: String?,
        @SerializedName("lat")
        val lat: Double?,
        @SerializedName("lon")
        val lon: Double?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("state")
        val state: String?,
    )
}
