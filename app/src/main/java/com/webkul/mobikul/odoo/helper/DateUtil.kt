package com.webkul.mobikul.odoo.helper

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun getStringDateMonth(timeStamp: String?): String? {
    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    return try {
        val date = inputDateFormat.parse(timeStamp)
        outputDateFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

fun getDeliveryLeadTime(saleDelay: Int): String? {
    var date = Date()
    val c = Calendar.getInstance()
    c.time = date
    c.add(Calendar.DATE,saleDelay + 1)
    date = c.time
    return SimpleDateFormat("dd MMMM", Locale.getDefault()).format(date)
}
