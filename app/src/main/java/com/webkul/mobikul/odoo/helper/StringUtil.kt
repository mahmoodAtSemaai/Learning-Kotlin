package com.webkul.mobikul.odoo.helper

import java.math.BigInteger


class StringUtil {

    companion object {
        private const val COMMA = ","
        private const val DOT = "."
        private const val ZEROS = "00"

        fun getPaymentAmount(amountInString: String): Long {
            val currencyRemoved = amountInString.subSequence(2, amountInString.length) as String
            val commasRemoved = currencyRemoved.replace(COMMA, "")
            val dotsRemoved = commasRemoved.replace(DOT, "")
            return dotsRemoved.toLong()
        }

        fun getPrice(priceInString: String, quantity: Int): Pair<String, String> {
            val amount = priceInString.subSequence(2, priceInString.length) as String
            val separatorsRemoved = amount.replace(DOT, "").replace(COMMA, "")
            var change =
                separatorsRemoved.substring(separatorsRemoved.length - 2, separatorsRemoved.length)
                    .toInt() * quantity
            val significantFigure = change / 100
            change %= 100
            val actual = (separatorsRemoved.subSequence(0, separatorsRemoved.length - 2) as String)
                .toBigInteger().times(quantity.toBigInteger())
                .plus(significantFigure.toBigInteger()).plus(if(change >= 50) BigInteger.ONE else BigInteger.ZERO)
            return Pair(actual.toString(), ZEROS)
        }

        fun getPriceInBahasa(priceInString: String, quantity: Int): String {
            val result = getPrice(priceInString, quantity)
            return String.format("%,d", result.first.toBigInteger())
                .replace(COMMA, DOT) + COMMA + result.second
        }

        fun getPriceInEnglish(priceInString: String, quantity: Int): String {
            val result = getPrice(priceInString, quantity)
            return String.format("%,d", result.first.toBigInteger()) + DOT + result.second
        }
    }

}