package com.webkul.mobikul.odoo.features.auth.presentation

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class LoginIntent {
    data class Login(val username: String ,val password: String) : LoginIntent()
}
