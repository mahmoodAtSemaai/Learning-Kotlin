package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentAcquirerMethodProvider(
    @SerializedName("payment_method_providers")
    @Expose
    val paymentMethodProvider: ArrayList<PaymentAcquirerMethod>
) : Parcelable