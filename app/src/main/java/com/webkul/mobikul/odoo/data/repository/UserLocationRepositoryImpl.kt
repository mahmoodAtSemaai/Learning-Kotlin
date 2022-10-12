package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserLocationEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserLocationRemoteDataSource
import com.webkul.mobikul.odoo.data.request.UserLocationRequest
import com.webkul.mobikul.odoo.data.response.UserLocationResponse
import com.webkul.mobikul.odoo.domain.repository.UserLocationRepository
import javax.inject.Inject

class UserLocationRepositoryImpl @Inject constructor(
    remoteDataSource: UserLocationRemoteDataSource
) : UserLocationRepository,
    BaseRepository<UserLocationEntity, Any, UserLocationRequest, UserLocationResponse>() {

    override val entityParser =
        ModelEntityParser(UserLocationEntity::class.java, UserLocationResponse::class.java)
    override var dataSource: IDataSource<UserLocationResponse, Any, UserLocationRequest> =
        remoteDataSource

    override suspend fun create(request: UserLocationRequest): Resource<UserLocationEntity> {
        return super<BaseRepository>.create(request)
    }
}