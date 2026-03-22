package com.mvproject.tvprogramguide.domain.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress

/**
 * Helper to interact with the Network connection
 * @property context application context
 */

class NetworkHelper(private val context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val activeNetwork
        get() = connectivityManager.activeNetwork

    private val networkCapabilities
        get() = connectivityManager.activeNetwork

    fun connectionState(): Flow<Boolean> {
        return callbackFlow {
            val callback =
                object : ConnectivityManager.NetworkCallback() {
                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities
                    ) {
                        super.onCapabilitiesChanged(network, networkCapabilities)

                        val isValidCapabilities = networkCapabilities.isNetworkCapabilitiesValid()

                        trySend(isValidCapabilities)
                    }

                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        val isConnected = checkIsInternetAvailable(network = network)
                        trySend(isConnected)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        trySend(false)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        trySend(false)
                    }
                }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
    }

    val isNetworkConnected: Boolean
        get() {
            val activeNetwork = activeNetwork ?: return false

            /*     val activeNetwork = connectivityManager
                     .getNetworkCapabilities(networkCapabilities) ?: return false

                 val result = when {
                     activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                     activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                     activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                     else -> false
                 }*/
            val isConnected = checkIsInternetAvailable(activeNetwork)
            return isConnected
        }
}

private fun checkIsInternetAvailable(network: Network): Boolean {
    return try {
        val socket = network.socketFactory.createSocket()
            ?: throw IOException("Socket is null.")
        socket.use {
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1200)
        }
        true
    } catch (ex: Throwable) {
        Timber.e("testing checkIsInternetAvailable error message ${ex.localizedMessage}")
        false
    }
}

private fun NetworkCapabilities?.isNetworkCapabilitiesValid(): Boolean = when {
    this == null -> false
    hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
            (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    ) -> true

    else -> false
}
