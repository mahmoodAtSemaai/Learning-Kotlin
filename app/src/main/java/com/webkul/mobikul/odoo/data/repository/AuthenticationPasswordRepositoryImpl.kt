package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OtpAuthenticationEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AuthenticationPasswordRemoteDataSource
import com.webkul.mobikul.odoo.data.request.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.data.response.OtpAuthenticationResponse
import com.webkul.mobikul.odoo.domain.repository.AuthenticationPasswordRepository
import javax.inject.Inject

class AuthenticationPasswordRepositoryImpl @Inject constructor(
    remoteDataSource: AuthenticationPasswordRemoteDataSource
) : AuthenticationPasswordRepository,
    BaseRepository<OtpAuthenticationEntity, Any, LoginOtpAuthenticationRequest, OtpAuthenticationResponse>() {

    override val entityParser
        get() = ModelEntityParser(
            OtpAuthenticationEntity::class.java,
            OtpAuthenticationResponse::class.java
        )

    override var dataSource: IDataSource<OtpAuthenticationResponse, Any, LoginOtpAuthenticationRequest> =
        remoteDataSource

    override suspend fun loginViaJWTToken(
        loginOtpAuthenticationRequest: LoginOtpAuthenticationRequest
    ): Resource<OtpAuthenticationEntity> {
        return super<BaseRepository>.create(loginOtpAuthenticationRequest)
    }

}