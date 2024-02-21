package com.mehdisekoba.weather.utils.permissions

data class PermissionRequestResult(
    val grantResults: Map<String, Boolean>,
    val shouldShowPermissionRationale: Map<String, Boolean>,
) {
    val isAnyGranted get() = grantResults.any { it.value }

    val isGranted get() = grantResults.all { it.value }

    val shouldShowRequestPermissionRationale get() = shouldShowPermissionRationale.any { it.value }
}
