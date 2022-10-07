package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CustomerGroupListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.CustomerGroupRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.CustomerGroupRepository
import javax.inject.Inject

class CustomerGroupRepositoryImpl @Inject constructor(
    remoteDataSource: CustomerGroupRemoteDataSource,
) : CustomerGroupRepository,
    BaseRepository<CustomerGroupListEntity, Any, Any, CustomerGroupListResponse>() {

    override val entityParser =
        ModelEntityParser(
            CustomerGroupListEntity::class.java,
            CustomerGroupListResponse::class.java
        )
    override var dataSource: IDataSource<CustomerGroupListResponse, Any, Any> = remoteDataSource

    override suspend fun get(): Resource<CustomerGroupListEntity> {
        return super<BaseRepository>.get()
    }
}