package com.webkul.mobikul.odoo.features.auth.data.remoteSource

import android.content.Context
import android.util.Log
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import javax.inject.Inject

class LoginRemoteDataSource @Inject constructor(private val apiService: AuthServices, private val context: Context) : BaseRemoteDataSource() {

    suspend fun logIn() = safeApiCall {
        Log.e("testing server", "request body ${RegisterDeviceTokenRequest(context).toString()}")
        apiService.signIn(RegisterDeviceTokenRequest(context).toString())
    }
}