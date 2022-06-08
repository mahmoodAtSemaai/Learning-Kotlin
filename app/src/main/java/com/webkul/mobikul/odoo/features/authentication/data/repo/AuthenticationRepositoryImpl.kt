package com.webkul.mobikul.odoo.features.authentication.data.repo

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.data.models.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationResponse
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.AuthenticationRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.domain.repo.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthenticationRemoteDataSource
) : AuthenticationRepository {
    override suspend fun validatePhoneNumber(phoneNumber: String): Resource<BaseOtpLoginResponse<Any>> {
        return remoteDataSource.validatePhoneNumber(phoneNumber)
    }

    override suspend fun generateOTP(phoneNumber: String): Resource<BaseOtpLoginResponse<Any>> {
        return remoteDataSource.generateOTP(phoneNumber)
    }

    override suspend fun loginViaOtpJWTToken(
        phoneNumber: String,
        otpAuthenticationRequest: OtpAuthenticationRequest
    ): Resource<BaseOtpLoginResponse<OtpAuthenticationResponse>> {
        return remoteDataSource.loginViaOtpJWTToken(phoneNumber, otpAuthenticationRequest)
    }

    override suspend fun loginViaJWTToken(
        phoneNumber: String,
        loginOtpAuthenticationRequest: LoginOtpAuthenticationRequest
    ): Resource<BaseOtpLoginResponse<OtpAuthenticationResponse>> {
        return remoteDataSource.loginViaJWTToken(phoneNumber, loginOtpAuthenticationRequest)
    }
}