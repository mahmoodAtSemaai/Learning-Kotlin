package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.address.StateListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProvinceServices
import javax.inject.Inject

class ProvinceRemoteDataSource @Inject constructor(
    private val apiService: ProvinceServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {
    suspend fun getStates(companyId: Int) = safeApiCall(StateListEntity::class.java) {
        apiService.getStates(companyId)
    }
}