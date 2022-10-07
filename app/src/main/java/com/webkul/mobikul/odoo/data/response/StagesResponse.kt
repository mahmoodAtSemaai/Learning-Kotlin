package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class StagesResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name : String,
    @SerializedName("stage_sequence") val order : String,
    @SerializedName("is_mandatory") val isMandatory : Boolean
)
