package com.webkul.mobikul.odoo.model.checkout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bank(
    val name: String,
    val type: String,
    val thumbnail: String,
    val account_number: String,
    val instructions: String
) : Parcelable {

    fun getAccountNumber(): String {
        return account_number.replace("...".toRegex(), "$0 ")
    }

}