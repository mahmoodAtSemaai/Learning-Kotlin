package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import android.content.Context
import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.FCMTokenServices
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import javax.inject.Inject

class FCMTokenRemoteDataSource @Inject constructor(
        private val apiServices: FCMTokenServices,
        private val context: Context,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun registerDeviceToken() = safeApiCall(Any::class.java){
        apiServices.registerDeviceToken(RegisterDeviceTokenRequest(context).toString())
    }

}