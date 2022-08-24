package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class TermsAndConditionsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("termsAndConditions") val termsAndConditions: String
)
