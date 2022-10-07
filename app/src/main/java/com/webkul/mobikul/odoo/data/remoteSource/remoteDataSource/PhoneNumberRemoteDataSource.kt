package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.PhoneNumberDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AuthenticationServices
import com.webkul.mobikul.odoo.data.response.UserCreateDateResponse
import javax.inject.Inject

class PhoneNumberRemoteDataSource @Inject constructor(
        private val apiService: AuthenticationServices,
        val appPreferences: AppPreferences,
        gson: Gson
) : PhoneNumberDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun getById(id: String) = safeApiCall(UserCreateDateResponse::class.java) {
        apiService.validatePhoneNumber(id)
    }

}