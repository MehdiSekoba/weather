@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.mehdisekoba.weather.ui.location.hourly

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.data.model.ResponseForecast.Data
import com.mehdisekoba.weather.databinding.ItemHourlyBinding
import com.mehdisekoba.weather.utils.base.BaseDiffUtils
import com.mehdisekoba.weather.utils.extensions.convertToDayNameEn
import com.mehdisekoba.weather.utils.extensions.convertToDayNameFa
import com.mehdisekoba.weather.utils.extensions.setDynamicallyIconWeather
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class HourlyAdapter
    @Inject
    constructor(
        @ApplicationContext private var context: Context,
    ) :
    RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
        private var items = emptyList<Data>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): ViewHolder {
            val binding = ItemHourlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            context = parent.context
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int,
        ) = holder.bind(items[position])

        override fun getItemCount() = items.size

        override fun getItemViewType(position: Int) = position

        override fun getItemId(position: Int) = position.toLong()

        inner class ViewHolder(private val binding: ItemHourlyBinding) :
            RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(item: Data) {
                binding.apply {
                    // Date
                    item.dtTxt?.let { date ->
                        if (Locale.getDefault().language.equals("fa")) {
                            val nameDay = date.split(" ")[0].convertToDayNameFa()
                            dayName.text = nameDay
                        } else {
                            val nameDay = date.split(" ")[0].convertToDayNameEn()
                            dayName.text = nameDay
                        }
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
                    item.main?.let { max ->
                        txtTempMax.text =
                            "${max.temp}${context.getString(R.string.degreeCelsius)}"
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
