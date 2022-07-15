package com.webkul.mobikul.odoo.data.entity


data class UserAnalyticsEntity(
    val email: String,
    val name: String,
    val isSeller: Boolean,
    val analyticsId: String
)
