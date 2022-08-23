package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.address.DistrictListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.DistrictServices
import javax.inject.Inject

class DistrictRemoteDataSource @Inject constructor(
    private val apiService: DistrictServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {

    suspend fun getDistricts(stateId: Int) = safeApiCall(DistrictListEntity::class.java) {
        apiService.getDistricts(stateId)
    }
}