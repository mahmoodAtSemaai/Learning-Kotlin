package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CustomerGroupListEntity
import com.webkul.mobikul.odoo.data.request.CustomerGroupRequest
import com.webkul.mobikul.odoo.data.entity.UserCustomerGroupEntity

interface CustomerGroupRepository : Repository {

    suspend fun getCustomerGroup(): Resource<CustomerGroupListEntity>

    suspend fun setUserCustomerGroup(
        userId: String,
        customerGroupRequest: CustomerGroupRequest
    ): Resource<UserCustomerGroupEntity>
}