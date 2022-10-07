package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.customer.address.AddressData

data class AddressEntity(
        val addresses: List<AddressData>,

        @SerializedName("tcount")
        val totalCount: Int,

        @SerializedName("default_shipping_address")
        val defaultShippingAddress: AddressData?,

)
