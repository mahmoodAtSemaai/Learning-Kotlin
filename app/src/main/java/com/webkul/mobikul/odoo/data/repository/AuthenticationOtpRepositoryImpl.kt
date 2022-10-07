package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OtpAuthenticationEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AuthenticationOtpRemoteDataSource
import com.webkul.mobikul.odoo.data.request.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.data.response.OtpAuthenticationResponse
import com.webkul.mobikul.odoo.domain.repository.AuthenticationOtpRepository
import javax.inject.Inject

class AuthenticationOtpRepositoryImpl @Inject constructor(
    remoteDataSource: AuthenticationOtpRemoteDataSource
) : AuthenticationOtpRepository,
    BaseRepository<OtpAuthenticationEntity, String, OtpAuthenticationRequest, OtpAuthenticationResponse>() {

    override val entityParser
        get() = ModelEntityParser(
            OtpAuthenticationEntity::class.java,
            OtpAuthenticationResponse::class.java
        )

    override var dataSource: IDataSource<OtpAuthenticationResponse, String, OtpAuthenticationRequest> =
        remoteDataSource

    override suspend fun loginViaOtpJWTToken(
        otpAuthenticationRequest: OtpAuthenticationRequest
    ): Resource<OtpAuthenticationEntity> {
        return super<BaseRepository>.create(otpAuthenticationRequest)
    }
}