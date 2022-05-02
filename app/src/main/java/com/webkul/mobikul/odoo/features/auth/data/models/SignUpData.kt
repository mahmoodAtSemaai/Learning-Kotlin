package com.webkul.mobikul.odoo.features.auth.presentation

import android.content.Context

data class SignUpData(
    var phoneNumber: String? = null,
    var name: String? = null,
    var password: String? = null,
    var confirmPassword: String? = null,
    var isTermAndCondition:Boolean = false,
    var isSeller:Boolean = false,
    var profileURL:String? = null,
    var country:String? = null,
)
