package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.DistrictListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.DistrictRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.DistrictRepository
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.DistrictListResponse
import javax.inject.Inject

class DistrictRepositoryImpl @Inject constructor(remoteDataSource: DistrictRemoteDataSource) :
    DistrictRepository, BaseRepository<DistrictListEntity, Int, Any, DistrictListResponse>() {

    override val entityParser =
        ModelEntityParser(DistrictListEntity::class.java, DistrictListResponse::class.java)
    override var dataSource: IDataSource<DistrictListResponse, Int, Any> = remoteDataSource

    override suspend fun getById(id: Int): Resource<DistrictListEntity> {
        return super<BaseRepository>.getById(id)
    }
}