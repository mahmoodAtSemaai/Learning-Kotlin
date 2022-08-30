package com.webkul.mobikul.odoo.data.request

import org.json.JSONObject

data class GetDiscountPriceRequest(
    val orderId: Int,
    val lineIds: List<Int>
) {
    override fun toString(): String {
        val jsonObject = JSONObject()
        jsonObject.put(ORDER_ID, orderId)
        jsonObject.put(LINE_IDS, lineIds.toString())
        return jsonObject.toString()
    }

    companion object{
        private const val ORDER_ID = "order_id"
        private const val LINE_IDS = "line_ids"
    }
}