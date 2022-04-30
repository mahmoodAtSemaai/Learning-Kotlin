package com.webkul.mobikul.odoo.model.checkout

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bank(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("thumbnail")
    @Expose
    val thumbnail: String,
    @SerializedName("account_number")
    @Expose
    val account_number: String,
    @SerializedName("provider_id")
    @Expose
    val providerId: Int

) : Parcelable {

    fun getAccountNumber(): String {
        return account_number.replace("...".toRegex(), "$0 ")
    }

}