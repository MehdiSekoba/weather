package com.mehdisekoba.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehdisekoba.weather.data.model.ResponsePollution
import com.mehdisekoba.weather.data.repository.InfoRepository
import com.mehdisekoba.weather.utils.network.NetworkRequest
import com.mehdisekoba.weather.utils.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel
    @Inject
    constructor(private val repository: InfoRepository) :
    ViewModel() {
        // Cities
        private val _pollutionData = MutableLiveData<NetworkRequest<ResponsePollution>>()
        val pollutionData: LiveData<NetworkRequest<ResponsePollution>> = _pollutionData

        fun callPollutionApi(
            lat: Double,
            lon: Double,
        ) = viewModelScope.launch {
            _pollutionData.value = NetworkRequest.Loading()
            val response = repository.getPollution(lat, lon)
            _pollutionData.value = NetworkResponse(response).generateResponse()
        }
    }
