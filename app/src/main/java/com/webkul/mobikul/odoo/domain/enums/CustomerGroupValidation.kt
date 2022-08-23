package com.webkul.mobikul.odoo.domain.enums

enum class CustomerGroupValidation(val value: Int) {
    TOKO_TANI(7),
    FARMERS_GROUP(6),
    FARMERS(8),
    OTHERS(9)
}
class CustomerGroupValidationException(private val validationType: String) : Exception(validationType)
