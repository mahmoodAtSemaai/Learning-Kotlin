package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse
import retrofit2.http.GET

interface TermsConditionServices {

    @GET(ApiInterface.MOBIKUL_TERM_AND_CONDITION)
    suspend fun getTermAndCondition() : TermAndConditionResponse

    @GET(ApiInterface.MOBIKUL_SELLER_TERM_AND_CONDITION)
    suspend fun getSellerTerms() : TermAndConditionResponse

}