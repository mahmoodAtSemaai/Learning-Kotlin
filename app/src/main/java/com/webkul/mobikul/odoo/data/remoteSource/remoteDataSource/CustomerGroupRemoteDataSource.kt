package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.CustomerGroupListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.CustomerGroupDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserCategoryServices
import com.webkul.mobikul.odoo.data.repository.CustomerGroupListResponse
import javax.inject.Inject

class CustomerGroupRemoteDataSource @Inject constructor(
    private val apiService: UserCategoryServices,
    gson: Gson,
    appPreferences: AppPreferences
) : CustomerGroupDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun get() = safeApiCall(CustomerGroupListResponse::class.java) {
        apiService.getCustomerGroups()
    }
}