package com.webkul.mobikul.odoo.ui.account.intent

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class EditAccountInfoIntent : IIntent {
    object Discard : EditAccountInfoIntent()
    object KeepEditing : EditAccountInfoIntent()
    data class BackNavigation(val value : String) :EditAccountInfoIntent()
    data class Save(val value:String) : EditAccountInfoIntent()
    data class EditValue(val value : String) : EditAccountInfoIntent()
    data class GetArgs(val arguments : Bundle?): EditAccountInfoIntent()
}