package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.VillageListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.VillageRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.VillageRepository
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.VillageListResponse
import javax.inject.Inject

class VillageRepositoryImpl @Inject constructor(remoteDataSource: VillageRemoteDataSource) :
    VillageRepository, BaseRepository<VillageListEntity, Int, Any, VillageListResponse>() {

    override val entityParser =
        ModelEntityParser(VillageListEntity::class.java, VillageListResponse::class.java)
    override var dataSource: IDataSource<VillageListResponse, Int, Any> = remoteDataSource

    override suspend fun getById(id: Int): Resource<VillageListEntity> {
        return super<BaseRepository>.getById(id)
    }
}