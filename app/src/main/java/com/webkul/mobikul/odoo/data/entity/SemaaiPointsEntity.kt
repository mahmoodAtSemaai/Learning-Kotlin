package com.webkul.mobikul.odoo.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SemaaiPointsEntity(
    val title : String,
    val value : Int
) : Parcelable
