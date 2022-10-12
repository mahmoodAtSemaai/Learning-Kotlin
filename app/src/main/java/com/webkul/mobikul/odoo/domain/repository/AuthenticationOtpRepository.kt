package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OtpAuthenticationEntity
import com.webkul.mobikul.odoo.data.request.OtpAuthenticationRequest

interface AuthenticationOtpRepository : Repository<OtpAuthenticationEntity, String, OtpAuthenticationRequest> {

    suspend fun loginViaOtpJWTToken(
            otpAuthenticationRequest: OtpAuthenticationRequest
    ): Resource<OtpAuthenticationEntity>

}