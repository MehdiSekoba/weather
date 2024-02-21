package com.mehdisekoba.weather.utils.other

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.mehdisekoba.weather.utils.extensions.isLocationEnabled
import com.mehdisekoba.weather.utils.extensions.openLocationSettings

object LocationHelper {
    fun requestLocationUpdates(
        context: Context,
        isNetworkAvailable: Boolean,
        onLocationReceived: (Double, Double) -> Unit,
        onError: () -> Unit,
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val result =
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token,
            )

        if (isNetworkAvailable) {
            if (!context.isLocationEnabled()) {
                context.openLocationSettings()
            } else {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    result.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val location = task.result
                            if (context.isLocationEnabled()) {
                                if (location != null) {
                                    onLocationReceived(location.latitude, location.longitude)
                                } else {
                                    onError()
                                }
                            }
                        } else {
                            onError()
                        }
                    }
                }
            }
        } else {
            onError()
        }
    }
}
