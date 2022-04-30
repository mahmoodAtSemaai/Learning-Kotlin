package com.webkul.mobikul.odoo.features.auth.presentation

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse

sealed class LoginState :IState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Login(val data: LoginResponse) : LoginState()
    data class Error(val error: String?) : LoginState()
}
