package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class GetSelectedItemsPriceResponse(
    @SerializedName("title") val title: String,
    @SerializedName("amount_untaxed") val amount_untaxed: String,
    @SerializedName("amount_tax") val amountTax: String,
    @SerializedName("grand_total") val grandTotal: String
)