package com.mehdisekoba.weather.data.repository

import com.mehdisekoba.weather.data.network.ApiServices
import com.mehdisekoba.weather.utils.METRIC
import javax.inject.Inject

class SearchCityRepository
    @Inject
    constructor(private val api: ApiServices) {
        // API
        suspend fun getCitiesList(q: String) = api.getCitiesList(q, METRIC)
    }
