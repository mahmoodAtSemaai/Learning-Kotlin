package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.data.request.UserAddressRequest
import com.webkul.mobikul.odoo.data.response.UserAddressResponse

interface UserAddressDataStore : IDataSource<UserAddressResponse, Any, UserAddressRequest>