package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class UserOnboardingStageEntity(
    @SerializedName("stage_id") val stageId : List<Int>,
    @SerializedName("user_id") val userId : Int,
    @SerializedName("customer_grp_id") val customerGroupId : Int,
    @SerializedName("customer_grp_name") val customerGroupName : String = ""
)
