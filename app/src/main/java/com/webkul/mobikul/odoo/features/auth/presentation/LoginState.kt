package com.webkul.mobikul.odoo.features.auth.presentation

import android.content.Intent
import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.features.auth.domain.enums.LoginFieldsValidation
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse

sealed class LoginState : IState {
    object Idle : LoginState()
    object Loading : LoginState()
    class PrivacyPolicy(val intent: Intent) : LoginState()
    data class Login(val data: LoginResponse) : LoginState()
    data class InvalidLoginDetailsError(val uiError: LoginFieldsValidation) : LoginState()
    data class Error(val error: String?) : LoginState()
}
