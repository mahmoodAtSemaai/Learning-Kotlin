package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.SubDistrictRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.SubDistrictRepository
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.SubDistrictListResponse
import javax.inject.Inject

class SubDistrictRepositoryImpl @Inject constructor(remoteDataSource: SubDistrictRemoteDataSource) :
    SubDistrictRepository,
    BaseRepository<SubDistrictListEntity, Int, Any, SubDistrictListResponse>() {

    override val entityParser =
        ModelEntityParser(SubDistrictListEntity::class.java, SubDistrictListResponse::class.java)
    override var dataSource: IDataSource<SubDistrictListResponse, Int, Any> = remoteDataSource
    override suspend fun getById(id: Int): Resource<SubDistrictListEntity> {
        return super<BaseRepository>.getById(id)
    }
}