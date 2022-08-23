package com.webkul.mobikul.odoo.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountInfoEntity(
    var name: String = "",
    val groupName: String = "",
    var customerGroupName: String = "",
    val phoneNumber: String = "",
    var userName : String = "",
    var isEdited : Boolean = false
) : Parcelable