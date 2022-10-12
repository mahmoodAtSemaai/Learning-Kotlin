package com.webkul.mobikul.odoo.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BankEntity(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("account_number")
    val accountNumb: String,
    @SerializedName("provider_id")
    val providerId: Int

) : Parcelable {

    fun getAccountNumber(): String {
        return accountNumb.replace("...".toRegex(), "$0 ")
    }

}