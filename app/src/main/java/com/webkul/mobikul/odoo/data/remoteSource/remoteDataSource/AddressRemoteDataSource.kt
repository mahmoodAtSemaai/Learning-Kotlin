package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.CountryEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AddressServices
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import javax.inject.Inject

class AddressRemoteDataSource @Inject constructor(
        private val apiService: AddressServices,
        gson: Gson
) : BaseRemoteDataSource(gson) {

    suspend fun getAddressBookData(baseLazyRequest: BaseLazyRequest) = safeApiCall(AddressEntity::class.java) {
        apiService.getAddressBookData(baseLazyRequest.toString())
    }

    suspend fun getCountryStateData() = safeApiCall(CountryEntity::class.java) {
        apiService.getCountryStateData()
    }
}