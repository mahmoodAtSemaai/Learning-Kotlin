package com.webkul.mobikul.odoo.features.authentication.presentation.state

import com.webkul.mobikul.odoo.core.mvicore.IState

sealed class LoginOptionsState : IState {
    object Idle : LoginOptionsState()
    object OtpLogin : LoginOptionsState()
    object PasswordLogin : LoginOptionsState()
    object ChangePhoneNumber : LoginOptionsState()
}
