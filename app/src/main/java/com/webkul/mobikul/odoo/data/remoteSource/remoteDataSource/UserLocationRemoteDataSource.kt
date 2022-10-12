package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.UserLocationDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserLocationServices
import com.webkul.mobikul.odoo.data.request.UserLocationRequest
import com.webkul.mobikul.odoo.data.response.UserLocationResponse
import javax.inject.Inject

class UserLocationRemoteDataSource @Inject constructor(
    private val apiService: UserLocationServices,
    gson: Gson,
    appPreferences: AppPreferences
) : UserLocationDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun create(request: UserLocationRequest) =
        safeApiCall(UserLocationResponse::class.java) {
            apiService.setUserLocation(
                request.partnerId,
                request.toString()
            )
        }
}