package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.UserAddressEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.UserAddressDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserAddressServices
import com.webkul.mobikul.odoo.data.request.UserAddressRequest
import com.webkul.mobikul.odoo.data.response.UserAddressResponse
import javax.inject.Inject

class UserAddressRemoteDataSource @Inject constructor(
    private val apiService: UserAddressServices,
    gson: Gson,
    appPreferences: AppPreferences
) : UserAddressDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun update(request: UserAddressRequest) =
        safeApiCall(UserAddressResponse::class.java) {
            apiService.setUserAddress(request.partnerId, request.toString())
        }
}