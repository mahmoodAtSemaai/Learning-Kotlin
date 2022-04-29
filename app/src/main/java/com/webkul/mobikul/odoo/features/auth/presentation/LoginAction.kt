package com.webkul.mobikul.odoo.features.auth.presentation

sealed class LoginAction {
    data class Login(val username: String ,val password: String) : LoginAction()
}
