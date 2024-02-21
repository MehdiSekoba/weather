@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.mehdisekoba.weather.ui.location.weekly

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mehdisekoba.weather.data.model.ResponseForecast.*
import com.mehdisekoba.weather.databinding.ItemForecastBinding
import com.mehdisekoba.weather.utils.base.BaseDiffUtils
import com.mehdisekoba.weather.utils.extensions.convertToDateNameFa
import com.mehdisekoba.weather.utils.extensions.setDynamicallyIconWeather
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class ForecastAdapter
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
        private var items = emptyList<Data>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): ViewHolder {
            val binding =
                ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int,
        ) = holder.bind(items[position])

        override fun getItemCount() = items.size

        override fun getItemViewType(position: Int) = position

        override fun getItemId(position: Int) = position.toLong()

        inner class ViewHolder(private val binding: ItemForecastBinding) :
            RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(item: Data) {
                binding.apply {
                    // Date
                    item.dtTxt?.let { date ->
                        val nameDay =
                            if (Locale.getDefault().language == "fa") {
                                date.substring(3, 10).convertToDateNameFa().replace("-", "/")
                            } else {
                                date.substring(5, 10).replace("-", "/")
                            }
                        dayDate.text = nameDay

                        if (date.isNotEmpty()) {
                            val hour = date.split(" ")[1].substring(0, 2)
                            hourDay.text = hour
                        }
                    }
                    item.weather?.let { weather ->
                        if (weather.isNotEmpty()) {
                            weather[0]?.let {
                                iconWeather.load(
                                    it.icon?.let { icon ->
                                        setDynamicallyIconWeather(
                                            icon,
                                        )
                                    },
                                )
                            }
                        }
                    }
                    item.main?.let { data ->
                        txtHumidity.text = "${data.humidity} %"
                    }

                    item.wind?.let { speed ->
                        txtGust.text = "${speed.gust}"
                    }
                }
            }
        }

        fun setData(data: List<Data>) {
            val adapterDiffUtils = BaseDiffUtils(items, data)
            val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
            items = data
            diffUtils.dispatchUpdatesTo(this)
        }
    }
