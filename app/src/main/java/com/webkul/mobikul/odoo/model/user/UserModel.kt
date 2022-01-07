package com.webkul.mobikul.odoo.model.user

data class UserModel(
    val email: String = "",
    val analyticsId: String = "",
    val name: String,
    val isSeller: Boolean
)