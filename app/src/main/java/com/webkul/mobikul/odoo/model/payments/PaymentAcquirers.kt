package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentAcquirers(
    @SerializedName("payment_acquirers")
    @Expose
    val acquirers: ArrayList<PaymentMethod>
) : Parcelable