package com.webkul.mobikul.odoo.features.auth.domain.enums

enum class SignUpFieldsValidation(val value: Int) {
    EMPTY_PHONE_NO(1),
    INVALID_PHONE_NO(2),
    EMPTY_PASSWORD(3),
    INVALID_PASSWORD(4),
    UNEQUAL_PASS_AND_CONFIRM_PASS(5),
    EMPTY_TERMS_CONDITIONS(6),
    EMPTY_PROFILE_URL(7),
    EMPTY_COUNTRY(8)
}

class SignUpValidationException(private val validationType: String) : Exception(validationType)