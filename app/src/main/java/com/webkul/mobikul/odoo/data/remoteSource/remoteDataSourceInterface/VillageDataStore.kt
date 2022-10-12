package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.VillageListResponse

interface VillageDataStore : IDataSource<VillageListResponse, Int, Any>