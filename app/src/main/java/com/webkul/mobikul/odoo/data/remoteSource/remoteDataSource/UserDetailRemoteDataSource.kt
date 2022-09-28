package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.UserDetailEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserDetailServices
import javax.inject.Inject

class UserDetailRemoteDataSource @Inject constructor(
    private val userDetailServices: UserDetailServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun get(userId: String) = safeApiCall(UserDetailEntity::class.java) {
        userDetailServices.getUserDetails(userId)
    }

}