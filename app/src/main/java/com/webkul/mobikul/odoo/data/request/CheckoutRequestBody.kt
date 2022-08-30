package com.webkul.mobikul.odoo.data.request

import org.json.JSONObject

data class CheckoutRequestBody(
        val lineIds: ArrayList<Int>,
        val pointsRedeemed: Boolean,
        val orderId: Int
) {

    fun getRequestBody() = JSONObject().apply {
        put(ITEMS, lineIds.toString().replace(" ", ""))
        put(POINTS_REDEEM, pointsRedeemed)
    }.toString()

    companion object {
        private const val ITEMS = "items"
        private const val POINTS_REDEEM = "pointsRedeemed"
    }
}