package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.StateListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.ProvinceRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.ProvinceRepository
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse
import javax.inject.Inject

class ProvinceRepositoryImpl @Inject constructor(private val remoteDataSource: ProvinceRemoteDataSource) :
    ProvinceRepository, BaseRepository<StateListEntity, Int, Any, StateListResponse>() {

    override val entityParser =
        ModelEntityParser(StateListEntity::class.java, StateListResponse::class.java)
    override var dataSource: IDataSource<StateListResponse, Int, Any> = remoteDataSource

    override suspend fun getById(id: Int): Resource<StateListEntity> {
        return super<BaseRepository>.getById(id)
    }
}