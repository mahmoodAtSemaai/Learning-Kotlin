package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.AddressFormEntity
import com.webkul.mobikul.odoo.data.entity.StateListEntity
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

    override suspend fun getAddressFormData(url: String): Resource<AddressFormEntity> {
        return remoteDataSource.getAddressFormData(url)
    }

    override suspend fun  getStates(companyId: Int): Resource<StateListEntity> {
        return remoteDataSource.getStates(companyId)
    }

}