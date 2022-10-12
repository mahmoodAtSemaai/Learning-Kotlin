package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.UserDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserServices
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.data.response.UserResponse
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val apiService: UserServices,
    gson: Gson,
    appPreferences: AppPreferences
) : UserDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun update(request: UserRequest) = safeApiCall(UserResponse::class.java) {
        apiService.updateUserDetails(
            request.userId.toString(),
            request.customerId ?: "",
            request.toString()
        )
    }
}