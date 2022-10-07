package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.request.CustomerGroupRequest
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.UserCustomerGroupDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserCategoryServices
import com.webkul.mobikul.odoo.data.response.UserCustomerGroupResponse
import javax.inject.Inject

class UserCustomerGroupRemoteDataSource @Inject constructor(
    private val apiService: UserCategoryServices,
    gson: Gson,
    appPreferences: AppPreferences
) : UserCustomerGroupDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun update(request: CustomerGroupRequest) =
        safeApiCall(UserCustomerGroupResponse::class.java) {
            apiService.setUserCustomerGroup(request.userId, request.toString())
        }

}