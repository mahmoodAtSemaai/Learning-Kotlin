package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.data.request.CustomerGroupRequest
import com.webkul.mobikul.odoo.data.response.UserCustomerGroupResponse

interface UserCustomerGroupDataStore :
    IDataSource<UserCustomerGroupResponse, Any, CustomerGroupRequest>