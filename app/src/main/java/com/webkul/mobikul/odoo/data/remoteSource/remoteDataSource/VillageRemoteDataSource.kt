package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.VillageListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.VillageDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.VillageServices
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.VillageListResponse
import javax.inject.Inject

class VillageRemoteDataSource @Inject constructor(
    private val apiService: VillageServices,
    gson: Gson,
    appPreferences: AppPreferences
) : VillageDataStore, BaseRemoteDataSource(gson, appPreferences) {
    override suspend fun getById(id: Int) = safeApiCall { apiService.getVillages(id) }
}