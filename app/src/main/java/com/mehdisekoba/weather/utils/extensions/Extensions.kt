package com.mehdisekoba.weather.utils.extensions

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.location.LocationManager
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.utils.other.TimeUtils
import com.mehdisekoba.weather.utils.permissions.PermissionsRequester
import com.mehdisekoba.weather.utils.permissions.PermissionsRequesterHolder
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.util.Locale

fun FragmentActivity.setStatusBarIconsColor(isDark: Boolean) {
    this.window.apply {
        WindowCompat.getInsetsController(this, this.decorView).apply {
            isAppearanceLightStatusBars = isDark
        }
    }
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun ImageView.loadImage(url: String) {
    this.load(url) {
        crossfade(true)
        crossfade(500)
        diskCachePolicy(CachePolicy.ENABLED)
        error(R.drawable.placeholder)
    }
}

fun RecyclerView.setupRecyclerview(
    myLayoutManager: RecyclerView.LayoutManager,
    myAdapter: RecyclerView.Adapter<*>,
) {
    this.apply {
        layoutManager = myLayoutManager
        setHasFixedSize(true)
        adapter = myAdapter
    }
}

fun <T> LiveData<T>.onceObserve(
    owner: LifecycleOwner,
    observe: Observer<T>,
) {
    observe(
        owner,
        object : Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observe.onChanged(value)
            }
        },
    )
}

fun View.isVisible(
    isShownLoading: Boolean,
    container: View,
) {
    if (isShownLoading) {
        this.isVisible = true
        container.isVisible = false
    } else {
        this.isVisible = false
        container.isVisible = true
    }
}

fun SpannableString.setClickableSpan(
    start: Int,
    end: Int,
    onClickAction: () -> Unit,
    color: Int = Color.BLUE,
) {
    val clickableSpan =
        object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClickAction()
            }
        }

    setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun TextView.setDynamicallyTintColor(
    tint: Int,
    color: Int,
) {
    // Start - Left = 0 || Top = 1 || End - Right = 2 || Bottom = 3
    this.compoundDrawables[tint].setTint(ContextCompat.getColor(context, color))
}

fun Context.openSettingWifi() {
    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
    startActivity(intent)
}

fun Context.openSettingDataMobile() {
    val intent = Intent(Settings.ACTION_SETTINGS)
    startActivity(intent)
}

fun Context.openLocationSettings() {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    startActivity(intent)
}

fun Context.isLocationEnabled(): Boolean {
    val locationManager: LocationManager =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
        locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER,
        )
}

fun Context.isDarkTheme(): Boolean = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

fun setDynamicallyIconWeather(icon: String): Int {
    return when (icon.dropLast(1)) {
        "01" -> {
            R.drawable.sunny
        }

        "02", "04" -> {
            R.drawable.partly_cloudy
        }

        "03" -> {
            R.drawable.cloudy
        }

        "09", "10" -> {
            R.drawable.rainy
        }

        "11" -> {
            R.drawable.thunderstroms
        }

        "13" -> {
            R.drawable.snow
        }

        "50" -> {
            R.drawable.haze
        }

        else -> 0
    }
}

fun setDynamicallyWallpaper(
    weatherView: WeatherView,
    icon: String,
): Int {
    return when (icon.dropLast(1)) {
        "01" -> {
            initWeatherView(weatherView, PrecipType.CLEAR)
            R.drawable.bg_sun
        }

        "02", "03", "04" -> {
            initWeatherView(weatherView, PrecipType.CLEAR)
            R.drawable.bg_cloud
        }

        "09", "10" -> {
            initWeatherView(weatherView, PrecipType.RAIN)
            R.drawable.bg_rain
        }

        "13" -> {
            initWeatherView(weatherView, PrecipType.SNOW)
            R.drawable.bg_snow
        }

        "50" -> {
            initWeatherView(weatherView, PrecipType.CLEAR)
            R.drawable.bg_haze
        }

        else -> 0
    }
}

private fun initWeatherView(
    weatherView: WeatherView,
    type: PrecipType,
) {
    weatherView.apply {
        setWeatherData(type)
        angle = 20
        emissionRate = 100.0f
    }
}

fun String.convertToDayNameFa(): String {
    val dateSplit = this.split("-")
    val timeUtils = TimeUtils(dateSplit[0].toInt(), dateSplit[1].toInt(), dateSplit[2].toInt())
    return timeUtils.weekDayStr
}

fun String.convertToDateNameFa(): String {
    val dateSplit = this.split("-")
    val timeUtils = TimeUtils(dateSplit[0].toInt(), dateSplit[1].toInt(), dateSplit[2].toInt())
    val iranianMonth = timeUtils.iranianMonth
    val iranianDay = timeUtils.iranianDay
    return "$iranianMonth/$iranianDay"
}

fun String.convertToDayNameEn(): String {
    val dateSplit = this.split("-")
    val year = dateSplit[0].toInt()
    val month = dateSplit[1].toInt()
    val day = dateSplit[2].toInt()
    val localDate = LocalDate.of(year, month, day)
    val dayOfWeek = localDate.dayOfWeek
    return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
}

fun Fragment.addOnBackPressedCallback(action: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                action.invoke()
            }
        },
    )
}

val Fragment.permissionsRequester
    get() = (activity as PermissionsRequesterHolder).permissionsRequester

@Suppress("UNCHECKED_CAST")
suspend inline fun PermissionsRequester.requestPermissions(vararg permissions: String) = requestPermissions(permissions as Array<String>)

fun MaterialAlertDialogBuilder.createAndShow() {
    val dialog = create()
    dialog.show()
}

fun Context.alert(dialogBuilder: MaterialAlertDialogBuilder.() -> Unit) {
    MaterialAlertDialogBuilder(this)
        .apply {
            setCancelable(false)
            dialogBuilder()
        }
        .createAndShow()
}
