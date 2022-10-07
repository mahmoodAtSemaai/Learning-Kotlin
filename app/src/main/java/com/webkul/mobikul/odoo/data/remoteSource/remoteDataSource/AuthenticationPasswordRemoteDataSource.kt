package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.AuthenticationPasswordDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AuthenticationServices
import com.webkul.mobikul.odoo.data.request.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.data.response.OtpAuthenticationResponse
import javax.inject.Inject

class AuthenticationPasswordRemoteDataSource @Inject constructor(
        private val apiService: AuthenticationServices,
        val appPreferences: AppPreferences,
        gson: Gson
) : AuthenticationPasswordDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun create(request: LoginOtpAuthenticationRequest) = safeApiCall(OtpAuthenticationResponse::class.java) {
        apiService.loginViaJWTToken(request.login, request.toString())
    }
}