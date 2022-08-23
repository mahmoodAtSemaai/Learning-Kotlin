package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.UserDetailsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserDetailsServices
import com.webkul.mobikul.odoo.data.request.UserDetailsRequest
import javax.inject.Inject

class UserDetailsRemoteDataSource @Inject constructor(
    private val apiService: UserDetailsServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {

    suspend fun setUserDetailsGroup(userId: String,customerId: String,userDetailsRequest: UserDetailsRequest) = safeApiCall(UserDetailsEntity::class.java) {
        apiService.setUserDetails(userId,customerId,userDetailsRequest.toString())
    }
}