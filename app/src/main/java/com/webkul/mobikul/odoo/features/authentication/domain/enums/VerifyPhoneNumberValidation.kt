package com.webkul.mobikul.odoo.features.authentication.domain.enums

enum class VerifyPhoneNumberValidation(val value: Int) {
    INCORRECT_PHONE_NUMBER_FORMAT(1),
    INCORRECT_PHONE_NUMBER(2)
}

class VerifyPhoneNumberException(private val validationType: String) : Exception(validationType)