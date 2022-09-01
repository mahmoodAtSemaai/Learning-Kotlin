package com.webkul.mobikul.odoo.data.entity

import android.os.Parcelable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellerEntity(

    @SerializedName("seller_id")
    @Expose
    val sellerId: Int,

    @SerializedName("seller_name")
    @Expose
    val sellerName: String,

    @SerializedName("items")
    @Expose
    val products: ArrayList<CartProductEntity>

) : Parcelable {

    var isSellerChecked: ObservableBoolean = ObservableBoolean(false)
    var productCount: ObservableInt = ObservableInt(products.size)


}

