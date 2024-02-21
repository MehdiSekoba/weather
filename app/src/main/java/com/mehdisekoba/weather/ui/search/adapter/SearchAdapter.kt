package com.mehdisekoba.weather.ui.search.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapePath
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.data.model.ResponseSearch
import com.mehdisekoba.weather.databinding.ItemSearchBinding
import com.mehdisekoba.weather.utils.base.BaseDiffUtils
import com.mehdisekoba.weather.utils.extensions.setDynamicallyIconWeather
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SearchAdapter
    @Inject
    constructor(
        @ApplicationContext private var context: Context,
    ) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
        private var items = emptyList<ResponseSearch>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): ViewHolder {
            val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

        inner class ViewHolder(private val binding: ItemSearchBinding) :
            RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(item: ResponseSearch) {
                binding.apply {
                    // ShapeAppearanceModel
                    val topLeftCornerRadius =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            30f,
                            context.resources.displayMetrics,
                        ).toInt()
                    val bottomYMargin =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            120f,
                            context.resources.displayMetrics,
                        ).toInt()
                    // create a new ShapeAppearanceModel
// create a new ShapeAppearanceModel
                    val shapeAppearanceModel =
                        ShapeAppearanceModel()
                            .toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, topLeftCornerRadius.toFloat())
                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                            .setBottomLeftCorner(CornerFamily.ROUNDED, 18f)
                            .setBottomRightCorner(CornerFamily.ROUNDED, 18f)
                            .setTopEdge(
                                object : EdgeTreatment() {
                                    override fun getEdgePath(
                                        length: Float,
                                        center: Float,
                                        interpolation: Float,
                                        shapePath: ShapePath,
                                    ) {
                                        shapePath.quadToPoint(
                                            0f,
                                            0f,
                                            length,
                                            (bgCities.layoutParams.height - bottomYMargin).toFloat(),
                                        )
                                    }
                                },
                            )
                            .build()
                    // create a new MaterialShapeDrawable based on above ShapeAppearanceModel and sets it color

                    val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
                    shapeDrawable.fillColor =
                        ContextCompat.getColorStateList(context, R.color.Blue_Magenta_Violet)

                    // finally use the ViewCompat static method to draw the above shapeDrawable to relativeLayout
                    ViewCompat.setBackground(bgCities, shapeDrawable)
                    // set data
                    item.weather?.let { weather ->
                        if (weather.isNotEmpty()) {
                            // imageview
                            weather[0]?.let {
                                weatherIcon.load(
                                    it.icon?.let { icon ->
                                        setDynamicallyIconWeather(
                                            icon,
                                        )
                                    },
                                )
                            }
                            // weather
                            txtWeather.text = weather[0]!!.description
                        }
                    }

                    item.main?.let { max ->
                        txtTemp.text =
                            "${max.temp}${context.getString(R.string.degreeCelsius)}"

                        TempInfoTxt.text =
                            "L:${max.tempMin}${context.getString(R.string.degree)}    " +
                            "H: ${max.tempMax}${context.getString(R.string.degree)}"
                    }
                    cityName.text = "${item.name}  " + "${item.sys!!.country}"
                    // Click
                    root.setOnClickListener {
                        // Click
                        onItemClickListener?.let {
                            it(item)
                        }
                    }
                }
            }
        }

        private var onItemClickListener: ((ResponseSearch) -> Unit)? = null

        fun setOnItemClickListener(listener: (ResponseSearch) -> Unit) {
            onItemClickListener = listener
        }

        fun setData(data: List<ResponseSearch>) {
            val adapterDiffUtils = BaseDiffUtils(items, data)
            val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
            items = data
            diffUtils.dispatchUpdatesTo(this)
        }
    }
