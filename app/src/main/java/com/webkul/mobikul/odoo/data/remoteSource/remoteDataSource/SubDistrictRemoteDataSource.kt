package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.SubDistrictServices
import javax.inject.Inject

class SubDistrictRemoteDataSource @Inject constructor(
    private val apiService: SubDistrictServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {
    suspend fun getSubDistricts(districtId: Int) = safeApiCall(SubDistrictListEntity::class.java) {
        apiService.getSubDistricts(districtId)
    }
}