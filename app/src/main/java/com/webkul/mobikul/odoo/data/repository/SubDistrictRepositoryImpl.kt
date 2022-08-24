package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.SubDistrictRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.SubDistrictRepository
import javax.inject.Inject

class SubDistrictRepositoryImpl @Inject constructor(private val remoteDataSource: SubDistrictRemoteDataSource) :
    SubDistrictRepository {
    override suspend fun getSubDistricts(districtId: Int): Resource<SubDistrictListEntity> {
        return remoteDataSource.getSubDistricts(districtId)
    }
}