package com.webkul.mobikul.odoo.ui.authentication.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.model.home.HomePageResponse

sealed class LoginPasswordEffect : IEffect {
    object NavigateToUserHomeActivity : LoginPasswordEffect()
}
