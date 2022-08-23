package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CustomerGroupListEntity
import com.webkul.mobikul.odoo.data.request.CustomerGroupRequest
import com.webkul.mobikul.odoo.data.entity.UserCustomerGroupEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.CustomerGroupRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.CustomerGroupRepository
import javax.inject.Inject

class CustomerGroupRepositoryImpl @Inject constructor(
    private val remoteDataSource: CustomerGroupRemoteDataSource,
    private val appPreferences: AppPreferences
) : CustomerGroupRepository {
    override suspend fun getCustomerGroup(): Resource<CustomerGroupListEntity> {
        return remoteDataSource.getCustomerGroups()
    }

    override suspend fun setUserCustomerGroup(
        userId: String,
        customerGroupRequest: CustomerGroupRequest
    ): Resource<UserCustomerGroupEntity> {
        val result = remoteDataSource.setUserCustomerGroup(userId, customerGroupRequest)
        when (result) {
            is Resource.Success -> {
                appPreferences.groupName =  customerGroupRequest.customerGrpName
                appPreferences.customerGroupId = customerGroupRequest.customerGrpType.toInt()
            }
        }
        return result
    }
}