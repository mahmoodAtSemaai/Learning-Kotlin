package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest

interface AddressDataStore : IDataSource<MyAddressesResponse, Any, BaseLazyRequest>