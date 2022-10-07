package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.data.entity.UserCustomerGroupEntity
import com.webkul.mobikul.odoo.data.request.CustomerGroupRequest

interface UserCustomerGroupRepository :
    Repository<UserCustomerGroupEntity, Any, CustomerGroupRequest>