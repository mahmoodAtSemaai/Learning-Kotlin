package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserAddressEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserAddressRemoteDataSource
import com.webkul.mobikul.odoo.data.request.UserAddressRequest
import com.webkul.mobikul.odoo.data.response.UserAddressResponse
import com.webkul.mobikul.odoo.domain.repository.UserAddressRepository
import javax.inject.Inject

class UserAddressRepositoryImpl @Inject constructor(private val remoteDataSource: UserAddressRemoteDataSource) :
    UserAddressRepository,
    BaseRepository<UserAddressEntity, Any, UserAddressRequest, UserAddressResponse>() {

    override val entityParser =
        ModelEntityParser(UserAddressEntity::class.java, UserAddressResponse::class.java)
    override var dataSource: IDataSource<UserAddressResponse, Any, UserAddressRequest> =
        remoteDataSource

    override suspend fun update(request: UserAddressRequest): Resource<UserAddressEntity> {
        return super<BaseRepository>.update(request)
    }
}