package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.StateListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.ProvinceRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.ProvinceRepository
import javax.inject.Inject

class ProvinceRepositoryImpl @Inject constructor(private val remoteDataSource: ProvinceRemoteDataSource) :
    ProvinceRepository {
    override suspend fun getStates(companyId: Int): Resource<StateListEntity> {
        return remoteDataSource.getStates(companyId)
    }
}