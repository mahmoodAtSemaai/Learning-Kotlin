package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.data.request.SignUpOtpAuthRequest
import com.webkul.mobikul.odoo.data.response.SignUpV1Response

interface SignUpAuthDataStore : IDataSource<SignUpV1Response, Any, SignUpOtpAuthRequest>