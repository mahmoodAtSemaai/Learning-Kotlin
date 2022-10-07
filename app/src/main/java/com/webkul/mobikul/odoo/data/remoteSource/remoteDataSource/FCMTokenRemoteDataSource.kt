package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.FCMTokenDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.FCMTokenServices
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import javax.inject.Inject

class FCMTokenRemoteDataSource @Inject constructor(
    private val apiServices: FCMTokenServices,
    gson: Gson,
    appPreferences: AppPreferences
) : FCMTokenDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun create(request: RegisterDeviceTokenRequest) =
        safeApiCall(Any::class.java) {
            apiServices.registerDeviceToken(request.toString())
        }
}