package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.UserLocationEntity
import com.webkul.mobikul.odoo.data.request.UserLocationRequest
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserLocationServices
import javax.inject.Inject

class UserLocationRemoteDataSource @Inject constructor(
    private val apiService: UserLocationServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun setUserLocation(partnerId: String, userLocationRequest: UserLocationRequest) =
        safeApiCall(UserLocationEntity::class.java) {
            apiService.setUserLocation(partnerId, userLocationRequest.toString())
        }
}