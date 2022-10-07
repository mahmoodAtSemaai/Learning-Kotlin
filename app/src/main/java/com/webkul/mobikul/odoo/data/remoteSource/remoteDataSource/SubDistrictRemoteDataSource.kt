package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.SubDistrictDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.SubDistrictServices
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.SubDistrictListResponse
import javax.inject.Inject

class SubDistrictRemoteDataSource @Inject constructor(
    private val apiService: SubDistrictServices,
    gson: Gson,
    appPreferences: AppPreferences
) : SubDistrictDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun getById(id: Int) = safeApiCall {
        apiService.getSubDistricts(id)
    }
}