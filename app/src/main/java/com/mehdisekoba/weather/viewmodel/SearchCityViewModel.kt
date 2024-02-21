package com.mehdisekoba.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehdisekoba.weather.data.model.ResponseSearch
import com.mehdisekoba.weather.data.repository.SearchCityRepository
import com.mehdisekoba.weather.utils.network.NetworkRequest
import com.mehdisekoba.weather.utils.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCityViewModel
    @Inject
    constructor(private val repository: SearchCityRepository) :
    ViewModel() {
        // Cities
        private val _searchData = MutableLiveData<NetworkRequest<ResponseSearch>>()
        val searchData: LiveData<NetworkRequest<ResponseSearch>> = _searchData

        fun callSearchApi(q: String) =
            viewModelScope.launch {
                _searchData.value = NetworkRequest.Loading()
                val response = repository.getCitiesList(q)
                _searchData.value = NetworkResponse(response).generateResponse()
            }
    }
