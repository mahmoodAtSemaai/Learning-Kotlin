package com.webkul.mobikul.odoo.features.auth.data.models

data class SignUpData(
    var phoneNumber: String = "",
    var name: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var isTermAndCondition:Boolean = false,
    var isSeller:Boolean = false,
    var profileURL:String = "",
    var country:Any? = "",
    var isMarketPlaceTermAndCondition:Boolean = false,

    )
