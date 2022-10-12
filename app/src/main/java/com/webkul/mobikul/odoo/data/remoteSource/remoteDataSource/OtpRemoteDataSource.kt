package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.OtpDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AuthenticationServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.OTPServices
import javax.inject.Inject

class OtpRemoteDataSource @Inject constructor(
    private val apiService: OTPServices,
    val appPreferences: AppPreferences,
    gson: Gson
) : OtpDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun create(request: String) = safeApiCall(Any::class.java) {
        apiService.generateOTPSignUp(request)
    }

    override suspend fun getById(id: String) = safeApiCall(Any::class.java) {
        apiService.generateOTP(id)
    }

}