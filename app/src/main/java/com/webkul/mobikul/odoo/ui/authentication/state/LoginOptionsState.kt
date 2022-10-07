package com.webkul.mobikul.odoo.ui.authentication.state

import com.webkul.mobikul.odoo.core.mvicore.IState

sealed class LoginOptionsState : IState {
    object Idle : LoginOptionsState()
    data class SetPhoneNumber(val phoneNumber: String, val enablePassword: Boolean) :
        LoginOptionsState()
}
