package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class GetCartId(
    @JvmField @SerializedName("cartId") val cartId: Int
)
