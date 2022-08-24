package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.ReferralResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ReferralCodeServices {

    @GET(ApiInterface.LOYALTY_VALIDATE_REFERRAL_CODE)
    suspend fun validateReferralCode(@Path(ApiInterface.REFERRAL) referralCode : String) : ReferralResponse

}