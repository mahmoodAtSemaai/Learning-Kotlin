package com.webkul.mobikul.odoo.data.entity

data class SignUpData(
    var phoneNumber: String = "",
    var name: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var isTermAndCondition:Boolean = false,
    var isSeller:Boolean = false,
    var profileURL:String = "",
    var country:String = "",
    var isMarketPlaceTermAndCondition:Boolean = false,

    )
