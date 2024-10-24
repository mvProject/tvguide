package com.mvproject.tvprogramguide.domain.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper class to interact with the Network connection.
 * This singleton class provides functionality to check the network connectivity status.
 *
 * @property context The application context, injected using Hilt.
 */
@Singleton
class NetworkHelper
@Inject
constructor(
    @ApplicationContext private val context: Context,
) {
    /**
     * Checks if the device is currently connected to a network.
     *
     * @return Boolean indicating whether the device is connected to a network (true) or not (false).
     */
    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        val result =
            when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }

        return result
    }
}
