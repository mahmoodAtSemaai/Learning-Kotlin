package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.DistrictListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DistrictServices {

    @GET(ApiInterface.MOBIKUL_EXTRAS_DISTRICT_DATA)
    suspend fun getDistricts(@Query(AddressAPIConstants.STATE_ID) stateId: Int): DistrictListResponse
}