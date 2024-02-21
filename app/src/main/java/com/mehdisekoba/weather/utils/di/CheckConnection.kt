package com.mehdisekoba.weather.utils.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CheckConnection {
    @Provides
    @Singleton
    fun provideCM(
        @ApplicationContext context: Context,
    ) = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideNR(): NetworkRequest =
        NetworkRequest.Builder().apply {
            addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            // Android M
            addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            // Android P
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                addCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)
            }
        }.build()

    @Provides
    @Singleton
    fun provideCheckVpn(
        manager: ConnectivityManager,
        request: NetworkRequest,
    ): Flow<Boolean> =
        callbackFlow {
            val callback =
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        channel.trySend(true)
                    }

                    override fun onLost(network: Network) {
                        channel.trySend(false)
                    }
                }

            manager.registerNetworkCallback(request, callback)

            awaitClose {
                manager.unregisterNetworkCallback(callback)
            }
        }
}
