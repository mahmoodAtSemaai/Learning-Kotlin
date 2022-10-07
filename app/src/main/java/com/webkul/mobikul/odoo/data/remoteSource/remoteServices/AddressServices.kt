package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants.COMPANY_ID
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.DistrictListResponse
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.SubDistrictListResponse
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.VillageListResponse
import retrofit2.http.*

interface AddressServices {

    @POST(ApiInterface.MOBIKUL_CUSTOMER_MY_ADDRESSES)
    suspend fun getAddress(@Body baseRequestJsonStr: String?): MyAddressesResponse

    @PUT(ApiInterface.MOBIKUL_UPDATE_ORDER_DATA)
    suspend fun updateAddressForOrder(@Path(ApiInterface.MOBIKUL_ORDER_ID) orderId: Int, @Body updateAddressRequest: String) : BaseResponse

    @DELETE
    suspend fun deleteAddress(@Url url: String) : BaseResponse

    @POST
    suspend fun getAddressFormData(@Url url: String) : AddressFormResponse

    @GET(ApiInterface.MOBIKUL_EXTRAS_STATE_DATA)
    suspend fun getStates(@Query(COMPANY_ID) companyId: Int) : StateListResponse

    @GET(ApiInterface.MOBIKUL_EXTRAS_DISTRICT_DATA)
    suspend fun getDistricts(@Query(AddressAPIConstants.STATE_ID) stateId: Int) : DistrictListResponse

    @GET(ApiInterface.MOBIKUL_EXTRAS_SUB_DISTRICT_DATA)
    suspend fun getSubDistricts(@Query(AddressAPIConstants.DISTRICT_ID) districtId: Int) : SubDistrictListResponse

    @GET(ApiInterface.MOBIKUL_EXTRAS_VILLAGE_DATA)
    suspend fun getVillages(@Query(AddressAPIConstants.SUBDISTRICT_ID) subDistrictId: Int) : VillageListResponse

    @PUT
    suspend fun editAddress(@Url url: String, @Body addressData: String) : BaseResponse

    @POST(ApiInterface.MOBIKUL_CUSTOMER_ADD_NEW_ADDRESS)
    suspend fun addNewAddress(@Body newAddressFormData: String) : BaseResponse


}