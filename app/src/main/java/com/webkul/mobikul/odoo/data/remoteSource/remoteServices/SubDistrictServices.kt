package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.SubDistrictListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SubDistrictServices {
    @GET(ApiInterface.MOBIKUL_EXTRAS_SUB_DISTRICT_DATA)
    suspend fun getSubDistricts(@Query(AddressAPIConstants.DISTRICT_ID) districtId: Int): SubDistrictListResponse
}