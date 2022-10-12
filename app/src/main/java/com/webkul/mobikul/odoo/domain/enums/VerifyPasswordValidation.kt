package com.webkul.mobikul.odoo.ui.authentication.domain.enums

import java.lang.Exception

enum class VerifyPasswordValidation(val value: Int) {
    INCORRECT_PASSWORD(1)
}

class VerifyPasswordException(private val validationType: String) : Exception(validationType)