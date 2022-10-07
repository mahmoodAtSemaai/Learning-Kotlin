package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.StateListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.ProvinceDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProvinceServices
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse
import javax.inject.Inject

class ProvinceRemoteDataSource @Inject constructor(
    private val apiService: ProvinceServices,
    gson: Gson,
    appPreferences: AppPreferences
) : ProvinceDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun getById(id: Int) = safeApiCall {
        apiService.getStates(id)
    }
}