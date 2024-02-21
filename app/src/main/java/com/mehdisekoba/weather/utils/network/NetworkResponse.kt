package com.mehdisekoba.weather.utils.network

import com.google.gson.Gson
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.data.model.ResponseErrors
import retrofit2.Response

open class NetworkResponse<T>(private val response: Response<T>) {
    open fun generateResponse(): NetworkRequest<T> {
        return when {
            response.code() == 404 ->
                NetworkRequest.Error(R.string.error_404.toString())
            response.code() == 429 -> NetworkRequest.Error(R.string.error_429.toString())
            response.code() == 401 -> {
                var errorMessage = ""
                if (response.errorBody() != null) {
                    val errorResponse =
                        Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ResponseErrors::class.java,
                        )
                    errorMessage = errorResponse.message.toString()
                }
                NetworkRequest.Error(errorMessage)
            }

            response.code() == 500 -> NetworkRequest.Error("Try again")
            response.isSuccessful -> NetworkRequest.Success(response.body()!!)
            else -> NetworkRequest.Error(response.message())
        }
    }
}
