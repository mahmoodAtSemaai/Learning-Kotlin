package com.webkul.mobikul.odoo.data.request

data class AppConfigRequest(
        var isAppRunFirstTime: Boolean? = null,
        var isAllowGuestCheckout: Boolean? = null
)
