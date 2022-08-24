package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProvinceServices {
    @GET(ApiInterface.MOBIKUL_EXTRAS_STATE_DATA)
    suspend fun getStates(@Query(AddressAPIConstants.COMPANY_ID) companyId: Int): StateListResponse
}