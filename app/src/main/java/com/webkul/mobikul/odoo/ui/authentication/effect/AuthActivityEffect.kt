package com.webkul.mobikul.odoo.ui.authentication.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class AuthActivityEffect : IEffect {
    object SetupPhoneScreen : AuthActivityEffect()
}
