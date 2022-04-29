package com.webkul.mobikul.odoo.features.auth.presentation

sealed class LoginIntent{
    data class Login(val username: String ,val password: String) : LoginIntent()
}
