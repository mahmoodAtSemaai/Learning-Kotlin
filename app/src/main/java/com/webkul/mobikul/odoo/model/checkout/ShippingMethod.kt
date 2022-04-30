package com.webkul.mobikul.odoo.model.checkout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ShippingMethod(
    var optionHeading: String,
    var optionDesc: String
) : Parcelable