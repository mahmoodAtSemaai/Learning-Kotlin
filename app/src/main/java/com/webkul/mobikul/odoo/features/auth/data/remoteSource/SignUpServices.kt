package com.webkul.mobikul.odoo.features.auth.data.remoteSource

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SignUpServices {

    @POST(ApiInterface.MOBIKUL_CUSTOMER_SIGN_UP)
    suspend fun signUp(@Body signUpRequestJsonStr: String?): SignUpResponse

    @POST(ApiInterface.MOBIKUL_CUSTOMER_MY_ADDRESSES)
    suspend fun getAddressBookData(@Body baseRequestJsonStr : String?) : MyAddressesResponse

    @POST(ApiInterface.MOBIKUL_EXTRAS_COUNTRY_STATE_DATA)
    suspend fun getCountryStateData() : CountryStateData

    @GET(ApiInterface.MOBIKUL_TERM_AND_CONDITION)
    suspend fun getTermAndCondition() : TermAndConditionResponse

    @GET(ApiInterface.MOBIKUL_SELLER_TERM_AND_CONDITION)
    suspend fun getSellerTerms() : TermAndConditionResponse


}