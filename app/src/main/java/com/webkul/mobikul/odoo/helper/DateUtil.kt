package com.webkul.mobikul.odoo.helper

import com.webkul.mobikul.odoo.constant.ApplicationConstant
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val ONE = 1
const val TWO = 2
const val THREE = 3
const val ELEVEN = 11
const val THIRTEEN = 13
const val TEN = 10

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

fun changeFormat(langCode: String?, date: String): String? {
    if (date == "False"){
        return ""
    }
    val dayStr = date.split("-".toRegex()).toTypedArray()[2]
    val day = dayStr.toInt()
    val suffix = getDayOfMonthSuffix(day)
    val format = SimpleDateFormat("yyyy-MM-dd")
    var newFormat = SimpleDateFormat("MMM, d yyyy")
    if (langCode == ApplicationConstant.INDONESIAN)
        newFormat = SimpleDateFormat("d MMM yyyy")
    var formattedDate: Date? = null
    try {
        formattedDate = format.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val newDate = formattedDate?.let { newFormat.format(it) }
    if (langCode == ApplicationConstant.INDONESIAN) {
        return if (formattedDate != null) {
            newDate
        } else {
            date
        }
    } else {
        if (newDate != null) {
            return if (formattedDate != null) {
                newDate.split(" ".toRegex()).toTypedArray()[0] + " " + newDate.split(" ".toRegex())
                    .toTypedArray()[1] + suffix + " " + newDate.split(" ".toRegex()).toTypedArray()[2]
            } else {
                date
            }
        }
    }
    return date
}

fun getDayOfMonthSuffix(n: Int): String {
    return if (n in ELEVEN..THIRTEEN) {
        "th"
    } else when (n % TEN) {
        ONE -> "st"
        TWO -> "nd"
        THREE -> "rd"
        else -> "th"
    }
}
