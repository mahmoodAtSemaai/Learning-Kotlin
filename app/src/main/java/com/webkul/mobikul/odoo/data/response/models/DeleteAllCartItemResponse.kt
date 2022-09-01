package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class DeleteAllCartItemResponse(
    @SerializedName("warning") val warning: String,
    @SerializedName("option_ids") val optionsId: ArrayList<Int>
)
