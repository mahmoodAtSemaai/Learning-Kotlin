package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.SignUpV1Entity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.SignUpAuthRemoteDataSource
import com.webkul.mobikul.odoo.data.request.SignUpOtpAuthRequest
import com.webkul.mobikul.odoo.data.response.BaseOtpSignUpResponse
import com.webkul.mobikul.odoo.data.response.SignUpV1Response
import com.webkul.mobikul.odoo.domain.repository.SignUpAuthRepository
import javax.inject.Inject

class SignUpAuthRepositoryImpl @Inject constructor(
    remoteDataSource: SignUpAuthRemoteDataSource
) : SignUpAuthRepository,
    BaseRepository<SignUpV1Entity, Any, SignUpOtpAuthRequest, SignUpV1Response>() {

    override val entityParser =
        ModelEntityParser(SignUpV1Entity::class.java, SignUpV1Response::class.java)
    override var dataSource: IDataSource<SignUpV1Response, Any, SignUpOtpAuthRequest> =
        remoteDataSource

    override suspend fun create(request: SignUpOtpAuthRequest): Resource<SignUpV1Entity> {
        return super<BaseRepository>.create(request)
    }
}