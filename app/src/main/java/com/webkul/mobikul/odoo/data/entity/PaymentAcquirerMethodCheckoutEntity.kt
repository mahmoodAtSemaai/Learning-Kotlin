package com.webkul.mobikul.odoo.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.payments.PaymentAcquirerMethods
import kotlinx.android.parcel.Parcelize

data class PaymentAcquirerMethodCheckoutEntity(
    @SerializedName("result")
    @Expose
    val paymentAcquirerMethods: PaymentAcquirerMethods,
)