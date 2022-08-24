package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.DistrictListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.DistrictRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.DistrictRepository
import javax.inject.Inject

class DistrictRepositoryImpl @Inject constructor(private val remoteDataSource: DistrictRemoteDataSource) :
    DistrictRepository {
    override suspend fun getDistricts(stateId: Int): Resource<DistrictListEntity> {
        return remoteDataSource.getDistricts(stateId)
    }
}