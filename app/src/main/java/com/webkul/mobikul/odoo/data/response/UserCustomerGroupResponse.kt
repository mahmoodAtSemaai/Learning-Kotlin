package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class UserCustomerGroupResponse(
    @SerializedName("customer_grp_id") val customerGrpId: Int
)
