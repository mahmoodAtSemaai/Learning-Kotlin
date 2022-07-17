package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AddressRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
        private val remoteDataSource: AddressRemoteDataSource
) : AddressRepository {

    override suspend fun getAddress(baseLazyRequest: BaseLazyRequest): Resource<AddressEntity> {
        return remoteDataSource.getAddress(baseLazyRequest)
    }

}