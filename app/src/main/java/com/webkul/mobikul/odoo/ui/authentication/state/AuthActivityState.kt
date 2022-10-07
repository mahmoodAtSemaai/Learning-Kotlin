package com.webkul.mobikul.odoo.ui.authentication.state

import com.webkul.mobikul.odoo.core.mvicore.IState

sealed class AuthActivityState : IState {
    object Idle : AuthActivityState()
    object SetBackStackListener : AuthActivityState()
    object InitToolBar : AuthActivityState()
    data class SetToolBarContent(val title: String, val showBackButton: Boolean) : AuthActivityState()
}
