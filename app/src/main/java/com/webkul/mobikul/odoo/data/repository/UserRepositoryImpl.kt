package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserEntity
import com.webkul.mobikul.odoo.data.localSource.localDataSource.UserLocalDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserRemoteDataSource
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.data.response.UserResponse
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository,
    BaseRepository<UserEntity, String, UserRequest, UserResponse>() {

    override val entityParser: ModelEntityParser<UserEntity, UserResponse>
        get() = ModelEntityParser(UserEntity::class.java, UserResponse::class.java)

    override var dataSource: IDataSource<UserResponse, String, UserRequest> = userLocalDataSource

    override suspend fun get(): Resource<UserEntity> {
        dataSource = userLocalDataSource
        return super<BaseRepository>.get()
    }

    override suspend fun update(request: UserRequest): Resource<UserEntity> {
        dataSource = userLocalDataSource
        return super<BaseRepository>.update(request)
    }

    override suspend fun updateUserDetails(userDetailsRequest: UserRequest): Resource<UserEntity> {
        dataSource = userRemoteDataSource
        return super<BaseRepository>.update(userDetailsRequest)
    }
}