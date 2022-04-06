package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentAcquirerMethods(
    @SerializedName("payment_acquirer_methods")
    @Expose
    val paymentAcquirerMethods: ArrayList<PaymentAcquirerMethod>
) : Parcelable