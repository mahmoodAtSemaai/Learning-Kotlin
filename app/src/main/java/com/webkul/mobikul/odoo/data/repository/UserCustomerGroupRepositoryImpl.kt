package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserCustomerGroupEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserCustomerGroupRemoteDataSource
import com.webkul.mobikul.odoo.data.request.CustomerGroupRequest
import com.webkul.mobikul.odoo.data.response.UserCustomerGroupResponse
import com.webkul.mobikul.odoo.domain.repository.UserCustomerGroupRepository
import javax.inject.Inject

class UserCustomerGroupRepositoryImpl @Inject constructor(
    remoteDataSource: UserCustomerGroupRemoteDataSource
) : UserCustomerGroupRepository,
    BaseRepository<UserCustomerGroupEntity, Any, CustomerGroupRequest, UserCustomerGroupResponse>() {

    override val entityParser =
        ModelEntityParser(
            UserCustomerGroupEntity::class.java,
            UserCustomerGroupResponse::class.java
        )
    override var dataSource: IDataSource<UserCustomerGroupResponse, Any, CustomerGroupRequest> =
        remoteDataSource

    override suspend fun update(request: CustomerGroupRequest): Resource<UserCustomerGroupEntity> {
        return super<BaseRepository>.update(request)
    }

}