package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.address.VillageListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.VillageServices
import javax.inject.Inject

class VillageRemoteDataSource @Inject constructor(
    private val apiService: VillageServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {
    suspend fun getVillages(subDistrictId: Int) = safeApiCall(VillageListEntity::class.java) {
        apiService.getVillages(subDistrictId)
    }
}