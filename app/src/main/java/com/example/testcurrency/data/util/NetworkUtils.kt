package com.example.testcurrency.data.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtils(private val context: Context) {

    fun hasNetworkConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE)  as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}