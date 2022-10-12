package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest

interface FCMTokenDataStore : IDataSource<Any,Any, RegisterDeviceTokenRequest> {
}