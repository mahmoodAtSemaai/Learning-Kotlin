package com.webkul.mobikul.odoo.ui.auth

import android.content.Intent
import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.LoginEntity
import com.webkul.mobikul.odoo.domain.enums.LoginFieldsValidation

sealed class LoginState : IState {
    object Idle : LoginState()
    object Loading : LoginState()
    class PrivacyPolicy(val intent: Intent) : LoginState()
    data class Login(val data: LoginEntity) : LoginState()
    data class InvalidLoginDetailsError(val uiError: LoginFieldsValidation) : LoginState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : LoginState()
    object ForgotPassword : LoginState()
    object NavigateToSignUp : LoginState()
    object CloseLoginScreen : LoginState()
    data class Dialog(val message: String?) : LoginState()
}
