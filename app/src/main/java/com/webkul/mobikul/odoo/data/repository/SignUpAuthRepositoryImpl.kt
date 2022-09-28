package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.response.BaseOtpSignUpResponse
import com.webkul.mobikul.odoo.data.request.SignUpOtpAuthRequest
import com.webkul.mobikul.odoo.data.entity.SignUpV1Entity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.SignUpAuthRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.SignUpAuthRepository
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import javax.inject.Inject

class SignUpAuthRepositoryImpl  @Inject constructor(
    private val appPreferences: AppPreferences,
    private val remoteDataSource: SignUpAuthRemoteDataSource
) : SignUpAuthRepository {

    override suspend fun generateOTP(phoneNumber: String): Resource<BaseOtpLoginResponse<Any>> {
        return remoteDataSource.generateOTP(phoneNumber)
    }

    override suspend fun loginViaOtpJWTToken(signUpOtpAuthRequest: SignUpOtpAuthRequest): Resource<BaseOtpSignUpResponse<SignUpV1Entity>> {
        val result = remoteDataSource.loginViaOtpJWTToken(signUpOtpAuthRequest)
        when (result) {
            is Resource.Success -> {
                saveCustomerCredentials(result.value.result.userId, result.value.result.customerId,result.value.result.auth)
            }
            is Resource.Default -> {}
            is Resource.Failure -> {
                saveCustomerCredentials("", "","")
                appPreferences.customerLoginToken = ""
            }
            is Resource.Loading -> {}
        }
        return result
    }

    private fun saveCustomerCredentials(userId: String, customerId: String, auth: String) {
        appPreferences.authToken = auth
        appPreferences.customerId = customerId
        appPreferences.userId = userId
    }
}