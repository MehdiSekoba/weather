package com.mehdisekoba.weather.ui.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import coil.load
import com.google.android.material.tabs.TabLayout
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.databinding.FragmentLocationBinding
import com.mehdisekoba.weather.ui.location.adapters.PagerAdapter
import com.mehdisekoba.weather.utils.base.BaseFragment
import com.mehdisekoba.weather.utils.extensions.isVisible
import com.mehdisekoba.weather.utils.extensions.setDynamicallyIconWeather
import com.mehdisekoba.weather.utils.extensions.setDynamicallyTintColor
import com.mehdisekoba.weather.utils.extensions.setDynamicallyWallpaper
import com.mehdisekoba.weather.utils.extensions.showSnackBar
import com.mehdisekoba.weather.utils.network.NetworkRequest
import com.mehdisekoba.weather.utils.other.LocationHelper
import com.mehdisekoba.weather.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LocationFragment : BaseFragment<FragmentLocationBinding>() {
    override val bindingInflater: (inflater: LayoutInflater) -> FragmentLocationBinding
        get() = FragmentLocationBinding::inflate

    @Inject
    lateinit var pagerAdapter: PagerAdapter

    // Other
    private val viewModel by viewModels<MainViewModel>()
    private val calendar by lazy { Calendar.getInstance() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        LocationHelper.requestLocationUpdates(
            requireContext(),
            isNetworkAvailable = isNetworkAvailable,
            onLocationReceived = { latitude, longitude ->
                viewModel.callGeoWeatherApi(
                    latitude,
                    longitude,
                )
                viewModel.callCurrentWeatherApi(
                    latitude,
                    longitude,
                    Locale.getDefault().language.toString(),
                )
                loadGeoWeatherData()
                loadCurrentWeatherData()
            },
            onError = {
                // Handle errors, such as showing a message to the user
                binding.root.showSnackBar(getString(R.string.checkYourNetwork))
            },
        )

        setupViewPager()
    }

    private fun loadGeoWeatherData() {
        binding.apply {
            viewModel.geoWeatherData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible(true, container)
                    }

                    is NetworkRequest.Success -> {
                        loading.isVisible(false, container)
                        response.data?.let { data ->
                            if (data.isNotEmpty()) {
                                cityName.text = data[0].name!!
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        loading.isVisible(false, container)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadCurrentWeatherData() {
        binding.apply {
            viewModel.currentWeatherData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible(true, container)
                    }

                    is NetworkRequest.Success -> {
                        loading.isVisible(false, container)

                        response.data?.let { data ->
                            // Weather
                            data.weather?.let { weather ->
                                if (weather.isNotEmpty()) {
                                    weather[0]?.let {
                                        // Info
                                        infoTxt.text = it.description
                                        // Bg
                                        bgImg.load(
                                            if (isNightNow()) {
                                                R.drawable.bg_night
                                            } else {
                                                it.icon?.let { icon ->
                                                    setDynamicallyWallpaper(
                                                        weatherView,
                                                        icon,
                                                    )
                                                }
                                            },
                                        ) {
                                            crossfade(true)
                                            crossfade(100)
                                        }
                                        weatherCity.load(
                                            if (isNightNow()) {
                                                R.drawable.night
                                            } else {
                                                it.icon?.let { icon ->
                                                    setDynamicallyIconWeather(
                                                        icon,
                                                    )
                                                }
                                            },
                                        )
                                    }
                                }
                            }
                            // Main
                            data.main?.let { main ->
                                tempTxt.text =
                                    "${main.temp}${getString(R.string.degreeCelsius)}"
                                TempInfoTxt.setDynamicallyTintColor(0, R.color.Blue_Magenta)
                                TempInfoTxt.setDynamicallyTintColor(2, R.color.yellow)
                                TempInfoTxt.text =
                                    "${main.tempMin}${getString(R.string.degree)}    " +
                                    "${main.tempMax}${getString(R.string.degree)}"
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

    private fun isNightNow(): Boolean {
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }

    private fun setupViewPager() {
        binding.apply {
            detailTabLayout.addTab(
                detailTabLayout.newTab().setText(getString(R.string.title_hours)),
            )
            detailTabLayout.addTab(detailTabLayout.newTab().setText(getString(R.string.title_week)))
            // View pager adapter
            detailViewPager.adapter = pagerAdapter

            // Select
            detailTabLayout.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        if (tab != null) detailViewPager.currentItem = tab.position
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                },
            )
            // View pager
            detailViewPager.registerOnPageChangeCallback(
                object : OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        detailTabLayout.selectTab(detailTabLayout.getTabAt(position))
                    }
                },
            )
            // Disable swipe
            detailViewPager.isUserInputEnabled = false
        }
    }
}
