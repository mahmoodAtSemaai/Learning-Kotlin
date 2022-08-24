package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.response.TermsAndConditionsResponse
import retrofit2.http.GET

interface TermsConditionServices {

    @GET(ApiInterface.MOBIKUL_TERM_AND_CONDITION)
    suspend fun getTermAndCondition() : TermsAndConditionsResponse

    @GET(ApiInterface.MOBIKUL_SELLER_TERM_AND_CONDITION)
    suspend fun getSellerTerms() : TermsAndConditionsResponse

}