package com.webkul.mobikul.odoo.ui.account.state

import com.webkul.mobikul.odoo.core.mvicore.IState

sealed class EditAccountInfoState : IState {
    object Idle : EditAccountInfoState()
    object ShowDialog : EditAccountInfoState()
    object KeepEditing : EditAccountInfoState()
    object EnableContinue : EditAccountInfoState()
    object DisableContinue : EditAccountInfoState()
    data class SetViews(val etValue : String,val title : String) : EditAccountInfoState()
}
