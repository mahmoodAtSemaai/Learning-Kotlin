package com.webkul.mobikul.odoo.model.checkout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransferInstruction(
    val heading: String?,
    val description: String?
) : Parcelable