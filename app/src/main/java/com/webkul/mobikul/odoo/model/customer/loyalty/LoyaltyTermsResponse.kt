package com.webkul.mobikul.odoo.model.customer.loyalty

import com.google.gson.annotations.SerializedName

data class LoyaltyTermsResponse (
    @SerializedName("success") val success: Boolean,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("terms_list") val termsList: List<LoyaltyTermsData>,
    @SerializedName("terms") val terms: LoyaltyTermsData,
)
