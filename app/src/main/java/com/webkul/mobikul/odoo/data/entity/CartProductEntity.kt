package com.webkul.mobikul.odoo.data.entity

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.BR
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartProductEntity(

        @SerializedName("lineId")
        @Expose
        val lineId: Int,

        @SerializedName("templateId")
        @Expose
        val templateId: Int,


        @SerializedName("product_id")
        @Expose
        val productId: Int,

        @SerializedName("name")
        @Expose
        val name: String,

        @SerializedName("thumbNail")
        @Expose
        val thumbNail: String,


        @SerializedName("priceUnit")
        @Expose
        val priceUnit: String,

        @SerializedName("priceReduce")
        @Expose
        val priceReduce: String,

        @SerializedName("qty")
        @Expose
        var quantity: Int,

        @SerializedName("total")
        @Expose
        val total: String,

        @SerializedName("discount")
        @Expose
        val discountPercent: String = "",

        @SerializedName("available_quantity")
        @Expose
        val availableQuantity: Int,

        @SerializedName("inventory_availability")
        @Expose
        val inventoryAvailability: String,

        @SerializedName("customer_lead")
        @Expose
        val leadTime: String,

        @SerializedName("is_wishlisted")
        @Expose
        var isWishListed: Boolean = false

) : Parcelable, BaseObservable() {

    var isChecked = ObservableBoolean(false)

    fun isOutOfStock(): Boolean {
        return (isAlways() || isThreshold()) && availableQuantity == 0
    }

    private fun isThreshold(): Boolean {
        return inventoryAvailability == ApplicationConstant.THRESHOLD
    }

    private fun isAlways(): Boolean {
        return inventoryAvailability == ApplicationConstant.ALWAYS
    }

    var localQuantity = 0

    init {
        localQuantity = quantity
    }

    @Bindable
    var bindingQuantity: Int = 0
        @Bindable
        get() {
            bindingQuantity = quantity
            return field
        }
        @Bindable
        set(value) {
            field = value
            notifyPropertyChanged(BR.bindingQuantity)
        }
}