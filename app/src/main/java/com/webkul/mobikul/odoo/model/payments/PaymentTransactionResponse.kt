package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentTransactionResponse(
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("status_code")
    @Expose
    val statusCode: Int,
    @SerializedName("result")
    @Expose
    val result: PaymentDetails,
    @SerializedName("message")
    @Expose
    val message: String
) : Parcelable
