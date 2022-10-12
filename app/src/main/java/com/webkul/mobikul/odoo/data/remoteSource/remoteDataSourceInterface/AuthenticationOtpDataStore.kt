package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.data.request.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.data.response.OtpAuthenticationResponse

interface AuthenticationOtpDataStore : IDataSource<OtpAuthenticationResponse, String, OtpAuthenticationRequest>