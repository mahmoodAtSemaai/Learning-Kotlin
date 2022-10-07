package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentStatusMetaEntity(
        @SerializedName("access_token")
        @Expose
        val accessToken: String,
        @SerializedName("account_number")
        @Expose
        val accountNumber: String,
        @SerializedName("created_at")
        @Expose
        val createdAt: String,
        @SerializedName("currency")
        @Expose
        val currency: String,
        @SerializedName("customer_id")
        @Expose
        val customerId: String,
        @SerializedName("expiration_time")
        @Expose
        val expirationTime: String,
        @SerializedName("is_live")
        @Expose
        val isLive: Boolean,
        @SerializedName("order_id")
        @Expose
        val orderId: String,
        @SerializedName("paid_amount")
        @Expose
        var paidAmount: String?,
        @SerializedName("payment_id")
        @Expose
        val paymentId: String,
        @SerializedName("payment_option")
        @Expose
        val paymentOption: String?,
        @SerializedName("pending_amount")
        @Expose
        val pendingAmount: String?,
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String?,
        @SerializedName("error")
        @Expose
        val error: String?,
        @SerializedName("paymentType")
        @Expose
        val paymentType: String?,
) {

    fun getFormattedAccountNumber(): String {
        return accountNumber.replace("...".toRegex(), "$0 ")
    }
}
