package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentResultEntity(
        @SerializedName("meta")
        @Expose
        val meta: PaymentStatusMetaEntity,
        @SerializedName("payment_status")
        @Expose
        val paymentStatus: String,
        @SerializedName("transaction_status")
        @Expose
        val transactionStatus: String,
        @SerializedName("Acquirer Name")
        @Expose
        val acquirer: String,
        @SerializedName("bank")
        @Expose
        val bank: BankEntity,
        @SerializedName("mobile_payments_mapping")
        @Expose
        val paymentStatusCode: Int,
        @SerializedName("expire_date")
        @Expose
        val expireDate: String?,
        @SerializedName("expire_time")
        @Expose
        val expireTime: String?,
)
