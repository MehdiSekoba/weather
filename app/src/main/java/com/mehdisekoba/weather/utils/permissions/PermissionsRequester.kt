package com.mehdisekoba.weather.utils.permissions

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.suspendCancellableCoroutine

class PermissionsRequester(private val activity: ComponentActivity) {
    private val awaitingResult = mutableMapOf<Set<String>, (PermissionRequestResult) -> Unit>()

    private val permissionLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { result ->
            val keys = result.keys
            awaitingResult.remove(keys)?.invoke(
                PermissionRequestResult(
                    grantResults = result,
                    shouldShowPermissionRationale =
                        keys.associateWith {
                            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
                        },
                ),
            )
        }

    suspend fun requestPermission(permission: String): PermissionRequestResult {
        return requestPermissions(arrayOf(permission))
    }

    suspend fun requestPermissions(permissions: Array<String>): PermissionRequestResult {
        permissionLauncher.launch(permissions)
        return suspendCancellableCoroutine { cont ->
            val permissionsSet = permissions.toSet()
            awaitingResult[permissionsSet] = { result ->
                cont.resumeWith(Result.success(result))
            }
            cont.invokeOnCancellation {
                awaitingResult.remove(permissionsSet)
            }
        }
    }

    fun requestPermission(
        lifecycleOwner: LifecycleOwner,
        permission: String,
        onGranted: (PermissionRequestResult) -> Unit,
    ) {
        requestPermissions(lifecycleOwner, arrayOf(permission), onGranted)
    }

    fun requestPermissions(
        lifecycleOwner: LifecycleOwner,
        permissions: Array<String>,
        onGranted: (PermissionRequestResult) -> Unit,
    ) {
        permissionLauncher.launch(permissions)
        val permissionsSet = permissions.toSet()
        awaitingResult[permissionsSet] = onGranted
        lifecycleOwner.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    awaitingResult.remove(permissionsSet)
                }
            },
        )
    }
}
