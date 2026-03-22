package com.mvproject.tvprogramguide.domain.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Helper class to interact with the Network connection.
 * This singleton class provides functionality to check the network connectivity status.
 *
 * @property context The application context, injected using Hilt.
 */
class NetworkHelper(private val context: Context) {
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
