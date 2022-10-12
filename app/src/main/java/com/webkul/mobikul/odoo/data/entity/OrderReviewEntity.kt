package com.webkul.mobikul.odoo.data.entity


import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.generic.KeyValuePairData


data class OrderReviewEntity (

    @SerializedName("transaction_id")
    val transactionId: Int = 0,

    @SerializedName("sale_order_id")
    val saleOrderId: Int = 0,

    @SerializedName("cart_count")
    val cartCount: Int = 0
)
