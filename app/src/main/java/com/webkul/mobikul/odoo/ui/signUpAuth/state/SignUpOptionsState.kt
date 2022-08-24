package com.webkul.mobikul.odoo.ui.signUpAuth.state

import com.webkul.mobikul.odoo.core.mvicore.IState

sealed class SignUpOptionsState : IState {
    object Idle : SignUpOptionsState()
    object OtpLogin : SignUpOptionsState()
    object ChangePhoneNumber : SignUpOptionsState()
}
