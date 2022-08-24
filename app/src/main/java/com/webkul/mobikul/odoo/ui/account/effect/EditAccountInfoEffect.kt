package com.webkul.mobikul.odoo.ui.account.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.data.entity.AccountInfoEntity

sealed class EditAccountInfoEffect : IEffect {
    data class NavigateToAccountInfo(val editValue : AccountInfoEntity) :
        EditAccountInfoEffect()
}