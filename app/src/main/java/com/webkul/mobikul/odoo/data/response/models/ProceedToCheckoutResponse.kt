package com.webkul.mobikul.odoo.data.response.models

data class ProceedToCheckoutResponse(
    val message: String,
    val result: Result,
    val status: String,
    val status_code: String,
    val success: Boolean
)