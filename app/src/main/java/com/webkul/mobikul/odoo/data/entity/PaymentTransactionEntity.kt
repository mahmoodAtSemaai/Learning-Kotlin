package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.payments.PaymentDetails

data class PaymentTransactionEntity(
    @SerializedName("result")
    @Expose
    val paymentDetails: PaymentDetailsEntity
)