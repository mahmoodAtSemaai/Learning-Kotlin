package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.VillageListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface VillageServices {
    @GET(ApiInterface.MOBIKUL_EXTRAS_VILLAGE_DATA)
    suspend fun getVillages(@Query(AddressAPIConstants.SUBDISTRICT_ID) subDistrictId: Int): VillageListResponse
}