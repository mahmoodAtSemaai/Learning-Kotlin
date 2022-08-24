package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.VillageListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.VillageRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.VillageRepository
import javax.inject.Inject

class VillageRepositoryImpl @Inject constructor(private val remoteDataSource: VillageRemoteDataSource) :
    VillageRepository {
    override suspend fun getVillages(subDistrictId: Int): Resource<VillageListEntity> {
        return remoteDataSource.getVillages(subDistrictId)
    }

}