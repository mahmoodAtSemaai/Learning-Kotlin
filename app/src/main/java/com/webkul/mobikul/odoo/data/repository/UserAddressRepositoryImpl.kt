package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserAddressEntity
import com.webkul.mobikul.odoo.data.request.UserAddressRequest
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserAddressRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.UserAddressRepository
import javax.inject.Inject

class UserAddressRepositoryImpl @Inject constructor(private val remoteDataSource: UserAddressRemoteDataSource) :
    UserAddressRepository {
    override suspend fun setUserAddress(
        partnerId: String,
        userAddressRequest: UserAddressRequest
    ): Resource<UserAddressEntity> {
        return remoteDataSource.setUserAddress(partnerId, userAddressRequest)
    }
}