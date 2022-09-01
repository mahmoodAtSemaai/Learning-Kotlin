package com.webkul.mobikul.odoo.data.request

import com.webkul.mobikul.odoo.data.entity.CartProductEntity
import org.json.JSONObject

data class CheckoutRequest(
    val selectedProducts: ArrayList<CartProductEntity>,
    val pointsRedeemed : Boolean
){
    fun getRequestBody() =  JSONObject().apply {
        put(ITEMS, selectedProducts)
        put(POINTS_REDEEM, pointsRedeemed)
    }.toString()

    companion object{
        private const val ITEMS = "items"
        private const val POINTS_REDEEM = "pointsRedeemed"
    }
}