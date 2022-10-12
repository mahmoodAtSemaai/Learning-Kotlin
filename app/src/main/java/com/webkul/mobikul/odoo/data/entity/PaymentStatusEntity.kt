package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.payments.Result

data class PaymentStatusEntity(
        @SerializedName("message")
        @Expose
        val message: String,
        @SerializedName("result")
        @Expose
        val paymentStatusResult: PaymentResultEntity
)
