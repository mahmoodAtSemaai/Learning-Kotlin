package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectedPaymentMethod(
    var paymentMethodText: String?,
    var paymentAcquirerId: String?,
    var paymentAcquirerProviderId: String?,
    var paymentAcquirerProviderType: String?,
    var isCOD: Boolean
) : Parcelable
