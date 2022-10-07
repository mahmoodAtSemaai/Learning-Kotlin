package com.webkul.mobikul.odoo.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.payments.PaymentAcquirerMethodProvider
import kotlinx.android.parcel.Parcelize

data class PaymentAcquirerMethodProviderCheckoutEntity(

    @SerializedName("result")
    @Expose
    val paymentAcquirerMethodProvider: PaymentAcquirerMethodProvider,

    )