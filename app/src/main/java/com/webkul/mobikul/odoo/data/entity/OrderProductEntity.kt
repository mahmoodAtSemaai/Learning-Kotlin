package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

class OrderProductEntity {

    val discount: String? = null
        get() = field ?: ""

    val state: String? = null
        get() {
            return field ?: ""
        }

    @SerializedName("price_total")
    val priceTotal: String? = null
        get() {
            return field ?: ""
        }

    @SerializedName("price_tax")
    val priceTax: String? = null
        get() {
            return field ?: ""
        }

    val name: String? = null
        get() {
            return field ?: ""
        }

    @SerializedName("product_name")
    private val productName: String? = null

    val url: String? = null
        get() {
            return field ?: ""
        }

    @SerializedName("price_unit")
    val priceUnit: String? = null
        get() {
            return field ?: ""
        }

    @SerializedName("price_subtotal")
    val priceSubtotal: String? = null
        get() {
            return field ?: ""
        }

    @SerializedName("product_id")
    val productId: String? = null
        get() {
            return field ?: ""
        }

    val templateId: String? = null
        get() {
            return field ?: ""
        }

    val qty: Double? = null

    val thumbNail: String? = null
        get() {
            return field ?: ""
        }

    @SerializedName("unit_type")
    val unitType: String? = null

    fun getProductName(): String {
        return if (productName == null || productName.isEmpty()) {
            name.toString()
        } else productName
    }

    fun getQuantity(): Int {
        return qty?.toInt() ?: 0
    }


}

