package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.data.response.TermsAndConditionsResponse
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse

interface TermsConditionDataStore : IDataSource<TermsAndConditionsResponse, Any, Any>