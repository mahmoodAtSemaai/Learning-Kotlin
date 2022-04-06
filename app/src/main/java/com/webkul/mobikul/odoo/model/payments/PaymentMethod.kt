package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class PaymentMethod(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("thumbnail")
    @Expose
    val thumbNail: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("is_mobikul_available")
    @Expose
    val isMobikulAvailable: Boolean
) : Parcelable
