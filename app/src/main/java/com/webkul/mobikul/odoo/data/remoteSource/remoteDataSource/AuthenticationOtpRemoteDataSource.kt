package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.AuthenticationOtpDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AuthenticationServices
import com.webkul.mobikul.odoo.data.request.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.data.response.OtpAuthenticationResponse
import javax.inject.Inject

class AuthenticationOtpRemoteDataSource @Inject constructor(
        private val apiService: AuthenticationServices,
        val appPreferences: AppPreferences,
        gson: Gson
) : AuthenticationOtpDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun create(request: OtpAuthenticationRequest) = safeApiCall(OtpAuthenticationResponse::class.java) {
        apiService.loginViaOtpJWTToken(request.phoneNumber, request.toString())
    }
}