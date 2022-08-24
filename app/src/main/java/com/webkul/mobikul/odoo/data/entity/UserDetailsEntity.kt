package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class UserDetailsEntity(
    @SerializedName("customer_grp_id") val customerGrpId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("group_name") val groupName: String,
    @SerializedName("profile_image") val profileImage: String,
    @SerializedName("partner_id") val customerId: Int,
)