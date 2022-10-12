package com.webkul.mobikul.odoo.ui.account.intent

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.request.UserRequest

sealed class AccountInfoIntent :IIntent {
    data class GetArgs(val arguments : Bundle?): AccountInfoIntent()

    object SetViews : AccountInfoIntent()
    data class EditName(val name : String,val title:String):AccountInfoIntent()
    data class EditGroupName(val groupName : String,val title:String):AccountInfoIntent()

    object Discard : AccountInfoIntent()
    object KeepEditing : AccountInfoIntent()
    object BackNavigation :AccountInfoIntent()
    data class Save(val updateUserDetails: UserRequest) : AccountInfoIntent()
    object GetUserIdCustomerId : AccountInfoIntent()
    object EditValue : AccountInfoIntent()
    object InitViews : AccountInfoIntent()
}