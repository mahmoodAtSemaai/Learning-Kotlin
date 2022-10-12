package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.data.response.UserResponse

interface UserDataStore : IDataSource<UserResponse, String, UserRequest>