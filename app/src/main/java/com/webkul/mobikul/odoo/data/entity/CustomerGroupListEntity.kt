package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class CustomerGroupListEntity(
    @SerializedName("customer_groups") val customerGroups : List<CustomerGroupEntity>
)
