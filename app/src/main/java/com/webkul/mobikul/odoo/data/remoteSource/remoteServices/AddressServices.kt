package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AddressServices {

    @POST(ApiInterface.MOBIKUL_CUSTOMER_MY_ADDRESSES)
    suspend fun getAddress(@Body baseRequestJsonStr: String?): MyAddressesResponse

}