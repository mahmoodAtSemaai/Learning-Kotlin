package com.webkul.mobikul.odoo.ui.account.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.AccountInfoEntity

sealed class AccountInfoState : IState {
    object Idle : AccountInfoState()
    object Loading : AccountInfoState()
    object ShowDialog : AccountInfoState()
    object KeepEditing : AccountInfoState()
    object GetArgs : AccountInfoState()
    object EnableContinue : AccountInfoState()
    object DisableContinue : AccountInfoState()
    data class SetData(val accountInfoEntity: AccountInfoEntity) : AccountInfoState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : AccountInfoState()
    object InitViews : AccountInfoState()
}