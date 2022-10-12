package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.DistrictDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.DistrictServices
import javax.inject.Inject

class DistrictRemoteDataSource @Inject constructor(
    private val apiService: DistrictServices,
    gson: Gson,
    appPreferences: AppPreferences
) : DistrictDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun getById(id: Int) = safeApiCall {
        apiService.getDistricts(id)
    }
}