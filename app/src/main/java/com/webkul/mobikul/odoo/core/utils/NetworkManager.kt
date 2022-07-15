package com.webkul.mobikul.odoo.core.utils

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

class NetworkManager @Inject constructor(private val context: Context) {

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
        }

}