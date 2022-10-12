package com.webkul.mobikul.odoo.data.repository

import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.data.response.CustomerGroupResponse

data class CustomerGroupListResponse(
    @SerializedName("customer_groups") val customerGroups: List<CustomerGroupResponse>
)
