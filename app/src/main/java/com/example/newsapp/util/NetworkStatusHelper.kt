package com.example.newsapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

const val TAG = "NetworkHelper"

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}

@Singleton
class NetworkStatusHelper @Inject constructor(@ApplicationContext private val context: Context) :
    LiveData<NetworkStatus>() {

    val validNetworkConnections: MutableSet<Network> = HashSet()
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    fun announceStatus() {
        Log.d(TAG, "announceStatus: ${validNetworkConnections.size}")
        if (validNetworkConnections.isNotEmpty()) {
            postValue(NetworkStatus.Available)
        } else {
            postValue(NetworkStatus.Unavailable)
        }
    }

    private fun getConnectivityManagerCallback() =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d(TAG, "onAvailable: available")
                val networkCapability = connectivityManager.getNetworkCapabilities(network)
                val hasNetworkConnection =
                    networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        ?: false
                if (hasNetworkConnection) {
                    validNetworkConnections.add(network)
                    announceStatus()
                }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.d(TAG, "onUnavailable: no connection")
                announceStatus()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d(TAG, "onLost: lost connection")
                validNetworkConnections.remove(network)
                announceStatus()
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                Log.d(TAG, "onCapabilitiesChanged: changed type")
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    validNetworkConnections.add(network)
                } else {
                    validNetworkConnections.remove(network)
                }
                announceStatus()
            }
        }


    override fun onActive() {
        super.onActive()
        Log.d(TAG, "onActive: started working")
        connectivityManagerCallback = getConnectivityManagerCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
        Log.d(TAG, "onInactive: stopped working")
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }


}