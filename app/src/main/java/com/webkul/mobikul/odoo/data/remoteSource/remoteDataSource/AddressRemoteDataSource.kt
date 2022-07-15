package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AddressServices
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import javax.inject.Inject

class AddressRemoteDataSource @Inject constructor(
        private val apiService: AddressServices,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {

    suspend fun getAddress(baseLazyRequest: BaseLazyRequest) = safeApiCall(AddressEntity::class.java) {
        apiService.getAddress(baseLazyRequest.toString())
    }

}