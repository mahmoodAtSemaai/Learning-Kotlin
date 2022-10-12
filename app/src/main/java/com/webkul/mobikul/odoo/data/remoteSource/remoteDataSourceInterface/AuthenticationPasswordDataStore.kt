package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.data.request.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.data.response.OtpAuthenticationResponse

interface AuthenticationPasswordDataStore : IDataSource<OtpAuthenticationResponse, Any, LoginOtpAuthenticationRequest>