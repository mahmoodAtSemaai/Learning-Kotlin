package com.webkul.mobikul.odoo.features.authentication.domain.repo

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.data.models.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationResponse

interface AuthenticationRepository : Repository {

    suspend fun validatePhoneNumber(phoneNumber: String): Resource<BaseOtpLoginResponse<Any>>

    suspend fun generateOTP(phoneNumber: String): Resource<BaseOtpLoginResponse<Any>>

    suspend fun loginViaOtpJWTToken(
        phoneNumber: String,
        otpAuthenticationRequest: OtpAuthenticationRequest
    ): Resource<BaseOtpLoginResponse<OtpAuthenticationResponse>>

    suspend fun loginViaJWTToken(
        phoneNumber: String,
        loginOtpAuthenticationRequest: LoginOtpAuthenticationRequest
    ): Resource<BaseOtpLoginResponse<OtpAuthenticationResponse>>
}