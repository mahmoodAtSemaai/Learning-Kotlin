package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.data.request.UserLocationRequest
import com.webkul.mobikul.odoo.data.response.UserLocationResponse

interface UserLocationDataStore : IDataSource<UserLocationResponse, Any, UserLocationRequest>