package com.webkul.mobikul.odoo.ui.account.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.data.entity.AccountInfoEntity
import com.webkul.mobikul.odoo.model.home.HomePageResponse

sealed class AccountInfoEffect : IEffect {
    data class NavigateToEditAccountInfo(
        val value: AccountInfoEntity,
        val title: String,
        val isName: Boolean
    ) : AccountInfoEffect()
    data class NavigateToAccount(val homePageResponse: HomePageResponse) : AccountInfoEffect()
}
