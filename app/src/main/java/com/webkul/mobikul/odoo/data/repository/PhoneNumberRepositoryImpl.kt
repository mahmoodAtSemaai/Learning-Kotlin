package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserCreateDateEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.PhoneNumberRemoteDataSource
import com.webkul.mobikul.odoo.data.response.UserCreateDateResponse
import com.webkul.mobikul.odoo.domain.repository.PhoneNumberRepository
import javax.inject.Inject

class PhoneNumberRepositoryImpl @Inject constructor(
    remoteDataSource: PhoneNumberRemoteDataSource
) : PhoneNumberRepository,
    BaseRepository<UserCreateDateEntity, String, Any, UserCreateDateResponse>() {

    override val entityParser
        get() = ModelEntityParser(UserCreateDateEntity::class.java, UserCreateDateResponse::class.java)

    override var dataSource: IDataSource<UserCreateDateResponse, String, Any> = remoteDataSource

    override suspend fun validatePhoneNumber(phoneNumber: String): Resource<UserCreateDateEntity> {
        return super<BaseRepository>.getById(phoneNumber)
    }

}