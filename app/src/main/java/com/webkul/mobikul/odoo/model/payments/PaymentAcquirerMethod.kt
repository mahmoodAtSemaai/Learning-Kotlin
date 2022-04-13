package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentAcquirerMethod(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("thumbnail")
    @Expose
    val imageUrl: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("instructions")
    @Expose
    val instructions: String = "",
    var vendorID: Int = 0
) : Parcelable
