package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.checkout.Bank
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(
    @SerializedName("meta")
    @Expose
    val meta: Meta,
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
    val bank: Bank,
    @SerializedName("mobile_payments_mapping")
    @Expose
    val paymentStatusCode: Int,
    @SerializedName("expire_date")
    @Expose
    val expireDate: String?,
    @SerializedName("expire_time")
    @Expose
    val expireTime: String?,
) : Parcelable