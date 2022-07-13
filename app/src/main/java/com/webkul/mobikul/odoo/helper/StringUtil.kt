package com.webkul.mobikul.odoo.helper


class StringUtil {

    companion object {

        fun getPaymentAmount(amountInString: String): Long {
            val currencyRemoved = amountInString.subSequence(2, amountInString.length) as String
            val commasRemoved = currencyRemoved.replace(",", "")
            val dotsRemoved = commasRemoved.replace(".", "")
            return dotsRemoved.toLong()
        }
    }


}