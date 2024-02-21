package com.mehdisekoba.weather.ui.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mehdisekoba.weather.data.model.PollutionModel
import com.mehdisekoba.weather.databinding.ItemPollutionBinding
import com.mehdisekoba.weather.utils.base.BaseDiffUtils
import javax.inject.Inject

class PollutionAdapter
    @Inject
    constructor() : RecyclerView.Adapter<PollutionAdapter.ViewHolder>() {
        private var items = emptyList<PollutionModel>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): ViewHolder {
            val binding = ItemPollutionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int,
        ) = holder.bind(items[position])

        override fun getItemCount() = items.size

        override fun getItemViewType(position: Int) = position

        override fun getItemId(position: Int) = position.toLong()

        inner class ViewHolder(private val binding: ItemPollutionBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: PollutionModel) {
                binding.apply {
                    pollutionTitle.text = item.title
                    pollutionCount.text = item.count.toString()
                }
            }
        }

        fun setData(data: List<PollutionModel>) {
            val adapterDiffUtils = BaseDiffUtils(items, data)
            val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
            items = data
            diffUtils.dispatchUpdatesTo(this)
        }
    }
