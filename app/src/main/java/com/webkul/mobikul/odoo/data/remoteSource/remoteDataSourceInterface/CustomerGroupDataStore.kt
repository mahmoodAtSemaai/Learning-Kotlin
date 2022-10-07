package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.data.repository.CustomerGroupListResponse

interface CustomerGroupDataStore :
    IDataSource<CustomerGroupListResponse, Any, Any>