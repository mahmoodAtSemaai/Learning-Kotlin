package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.response.BaseOtpSignUpResponse
import com.webkul.mobikul.odoo.data.request.SignUpOtpAuthRequest
import com.webkul.mobikul.odoo.data.entity.SignUpV1Entity
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse

interface SignUpAuthRepository : Repository {

    suspend fun generateOTP(phoneNumber: String): Resource<BaseOtpLoginResponse<Any>>

    suspend fun loginViaOtpJWTToken(
        signUpOtpAuthRequest: SignUpOtpAuthRequest
    ): Resource<BaseOtpSignUpResponse<SignUpV1Entity>>

}