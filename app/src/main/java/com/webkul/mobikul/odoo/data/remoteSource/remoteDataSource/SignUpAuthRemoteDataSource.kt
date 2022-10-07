package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.SignUpAuthDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.SignUpAuthServices
import com.webkul.mobikul.odoo.data.request.SignUpOtpAuthRequest
import com.webkul.mobikul.odoo.data.response.SignUpV1Response
import javax.inject.Inject

class SignUpAuthRemoteDataSource @Inject constructor(
    private val apiService: SignUpAuthServices,
    gson: Gson,
    appPreferences: AppPreferences
) : SignUpAuthDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun create(request: SignUpOtpAuthRequest) =
        safeApiCall(SignUpV1Response::class.java) {
            apiService.signUpWithOTP(request.toString())
        }
}