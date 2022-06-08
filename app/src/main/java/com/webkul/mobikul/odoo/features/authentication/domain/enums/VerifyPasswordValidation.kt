package com.webkul.mobikul.odoo.features.authentication.domain.enums

import java.lang.Exception

enum class VerifyPasswordValidation(val value: Int) {
    EMPTY_PASSWORD(1),
    INCORRECT_PASSWORD(2)
}

class VerifyPasswordException(private val validationType: String) : Exception(validationType)