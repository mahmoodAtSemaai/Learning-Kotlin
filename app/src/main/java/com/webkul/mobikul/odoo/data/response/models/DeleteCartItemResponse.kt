package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class DeleteCartItemResponse(
    @SerializedName("line_id") val lineId: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("warning") val warning: String,
    @SerializedName("option_ids") val optionsId: ArrayList<Int>
)