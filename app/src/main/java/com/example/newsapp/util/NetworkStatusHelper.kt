package com.example.newsapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.Network
import android.net.NetworkCapabilities.*
import android.os.Build
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

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val connectivityManagerCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(NetworkStatus.Available)
                Log.d(TAG, "onAvailable: available")
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.d(TAG, "onUnavailable: no connection")
                postValue(NetworkStatus.Unavailable)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d(TAG, "onLost: lost connection")
                postValue(NetworkStatus.Unavailable)
            }

            /* override fun onCapabilitiesChanged(
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
             }*/
        }

    private fun checkConnection(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    override fun onActive() {
        super.onActive()
        val hasInternet = checkConnection()
        if (hasInternet) {
            postValue(NetworkStatus.Available)
        } else {
            postValue(NetworkStatus.Unavailable)
        }
        /*val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        Log.d(
            TAG,
            "onActive: ${networkRequest.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)}"
        )
        connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback)
        //connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)*/
    }

    override fun onInactive() {
        super.onInactive()
        Log.d(TAG, "onInactive: stopped working")
        //connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

}