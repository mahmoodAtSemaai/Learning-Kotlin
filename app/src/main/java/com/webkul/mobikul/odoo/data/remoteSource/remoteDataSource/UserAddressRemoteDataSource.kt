package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.UserAddressEntity
import com.webkul.mobikul.odoo.data.request.UserAddressRequest
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserAddressServices
import javax.inject.Inject

class UserAddressRemoteDataSource @Inject constructor(
    private val apiService: UserAddressServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun setUserAddress(partnerId: String, userAddressRequest: UserAddressRequest) =
        safeApiCall(UserAddressEntity::class.java) {
            apiService.setUserAddress(partnerId, userAddressRequest.toString())
        }
}