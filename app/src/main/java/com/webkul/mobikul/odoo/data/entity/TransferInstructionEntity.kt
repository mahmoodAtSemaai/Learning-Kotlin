package com.webkul.mobikul.odoo.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransferInstructionEntity(
        @SerializedName("name")
        @Expose
        val heading: String,
        @SerializedName("instructions")
        @Expose
        val instruction: String,
        @SerializedName("provider_id")
        @Expose
        val providerId: Int,
        var isInstructionVisible: Boolean = false
) : Parcelable
