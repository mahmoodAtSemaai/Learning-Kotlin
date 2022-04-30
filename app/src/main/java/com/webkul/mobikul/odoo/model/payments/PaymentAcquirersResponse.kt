package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentAcquirersResponse(
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("status_code")
    @Expose
    val statusCode: String,
    @SerializedName("result")
    @Expose
    val result: PaymentAcquirers,
    @SerializedName("message")
    @Expose
    val message: String
) : Parcelable