package com.pawthunder.currencyexample.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class NetworkConnectionProvider @Inject constructor(private val context: Context) {

    val isConnected = MutableLiveData<Boolean>()

    init {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)?.let {
           checkCurrentConnection(it)
           addCallBacks(it)
       }
    }

    private fun checkCurrentConnection(manager: ConnectivityManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            isConnected.value = manager.activeNetworkInfo?.isConnected == true
        } else {
            val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
            isConnected.value = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        }
    }

    private fun addCallBacks(manager: ConnectivityManager) {
        val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected.postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected.postValue(false)
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            val builder = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            manager.registerNetworkCallback(builder.build(), connectivityCallback)
        } else {
            manager.registerDefaultNetworkCallback(connectivityCallback)
        }
    }

}
