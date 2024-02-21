package com.mehdisekoba.weather.ui.location.hourly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.data.model.ResponseForecast
import com.mehdisekoba.weather.databinding.FragmentHourlyBinding
import com.mehdisekoba.weather.utils.base.BaseFragment
import com.mehdisekoba.weather.utils.extensions.isVisible
import com.mehdisekoba.weather.utils.extensions.setupRecyclerview
import com.mehdisekoba.weather.utils.extensions.showSnackBar
import com.mehdisekoba.weather.utils.network.NetworkRequest
import com.mehdisekoba.weather.utils.other.LocationHelper
import com.mehdisekoba.weather.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HourlyFragment : BaseFragment<FragmentHourlyBinding>() {
    override val bindingInflater: (inflater: LayoutInflater) -> FragmentHourlyBinding
        get() = FragmentHourlyBinding::inflate

    @Inject
    lateinit var hourlyAdapter: HourlyAdapter

    // other
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // Call the LocationHelper to request location updates
        LocationHelper.requestLocationUpdates(
            requireContext(),
            isNetworkAvailable = isNetworkAvailable,
            onLocationReceived = { latitude, longitude ->
                viewModel.callForecastApi(
                    latitude,
                    longitude,
                    Locale.getDefault().language.toString(),
                )
                loadForecastData()
            },
            onError = {
                // Handle errors, such as showing a message to the user
                binding.root.showSnackBar(getString(R.string.checkYourNetwork))
            },
        )
    }

    private fun loadForecastData() {
        binding.apply {
            viewModel.forecastData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible(true, container)
                    }

                    is NetworkRequest.Success -> {
                        loading.isVisible(false, container)
                        response.data?.let { data ->
                            if (data.list!!.isNotEmpty()) {
                                initRecyclerView(data.list)
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        loading.isVisible(false, container)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initRecyclerView(cities: List<ResponseForecast.Data>) {
        hourlyAdapter.setData(cities)
        binding.rcHour.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            hourlyAdapter,
        )
    }
}
