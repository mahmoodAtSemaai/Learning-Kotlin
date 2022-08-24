package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.request.SignUpOtpAuthRequest
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.SignUpAuthServices
import javax.inject.Inject

class SignUpAuthRemoteDataSource @Inject constructor(
    private val apiService: SignUpAuthServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {

    suspend fun generateOTP(phoneNumber: String) = safeApiCall {
        apiService.generateOTP(phoneNumber)
    }

    suspend fun loginViaOtpJWTToken(
        signUpOtpAuthRequest: SignUpOtpAuthRequest
    ) = safeApiCall {
        apiService.signUpWithOTP(signUpOtpAuthRequest.toString())
    }

}