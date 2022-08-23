package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.CustomerGroupListEntity
import com.webkul.mobikul.odoo.data.request.CustomerGroupRequest
import com.webkul.mobikul.odoo.data.entity.UserCustomerGroupEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserCategoryServices
import javax.inject.Inject

class CustomerGroupRemoteDataSource @Inject constructor(
    private val apiService: UserCategoryServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun getCustomerGroups() = safeApiCall(CustomerGroupListEntity::class.java) {
        apiService.getCustomerGroups()
    }

    suspend fun setUserCustomerGroup(userId: String, customerGroupRequest: CustomerGroupRequest) =
        safeApiCall(
            UserCustomerGroupEntity::class.java
        ) {
            apiService.setUserCustomerGroup(userId, customerGroupRequest.toString())
        }
}