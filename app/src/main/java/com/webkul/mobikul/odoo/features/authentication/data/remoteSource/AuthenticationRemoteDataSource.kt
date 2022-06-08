package com.webkul.mobikul.odoo.features.authentication.data.remoteSource

import android.annotation.SuppressLint
import android.util.Log
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.data.models.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationRequest
import javax.inject.Inject

class AuthenticationRemoteDataSource @Inject constructor(
    private val apiService: AuthenticationServices
) :
    BaseRemoteDataSource() {

    suspend fun validatePhoneNumber(phoneNumber: String) = safeApiCall {
        apiService.validatePhoneNumber(phoneNumber)
    }

    suspend fun generateOTP(phoneNumber: String) = safeApiCall {
        apiService.generateOTP(phoneNumber)
    }

    suspend fun loginViaOtpJWTToken(
        phoneNumber: String,
        otpAuthenticationRequest: OtpAuthenticationRequest
    ) = safeApiCall {
        apiService.loginViaOtpJWTToken(phoneNumber, otpAuthenticationRequest.toString())
    }

    suspend fun loginViaJWTToken(
        phoneNumber: String,
        loginOtpAuthenticationRequest: LoginOtpAuthenticationRequest
    ) = safeApiCall {
        apiService.loginViaJWTToken(phoneNumber, loginOtpAuthenticationRequest.toString())
    }
}