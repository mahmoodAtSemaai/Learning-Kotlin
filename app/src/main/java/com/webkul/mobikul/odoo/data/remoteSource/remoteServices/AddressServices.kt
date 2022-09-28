package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse
import io.reactivex.Observable
import retrofit2.http.*

interface AddressServices {

    @POST(ApiInterface.MOBIKUL_CUSTOMER_MY_ADDRESSES)
    suspend fun getAddress(@Body baseRequestJsonStr: String?): MyAddressesResponse


    @POST
    suspend fun getAddressFormData(@Url url: String?): AddressFormResponse

    @GET(ApiInterface.MOBIKUL_EXTRAS_STATE_DATA)
    suspend fun getStates(@Query(AddressAPIConstants.COMPANY_ID) company_id: Int): StateListResponse

}