package com.webkul.mobikul.odoo.ui.signUpOnboarding.state

import com.webkul.mobikul.odoo.core.mvicore.IState

sealed class NoInternetState : IState {
    object Idle : NoInternetState()
    object Refresh : NoInternetState()
}