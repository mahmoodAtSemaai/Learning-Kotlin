package com.webkul.mobikul.odoo.features.authentication.data.models

import org.json.JSONObject

data class LoginOtpAuthenticationRequest(
    var login: String = "",
    var password: String = "",
    var fcmToken: String = "",
    var customerId: String = "",
    var fcmDeviceId: String = "",
    var name: String = "",
    var isSocialLogin: Boolean = false

) {
    override fun toString(): String {
        val valJsonObject = JSONObject()
        if (login.isNotBlank()) {
            valJsonObject.put("login", login)
        }
        if (password.isNotBlank()) {
            valJsonObject.put("password", password)
        }
        if (fcmToken.isNotBlank()) {
            valJsonObject.put("fcmToken", fcmToken)
        }
        if (customerId.isNotBlank()) {
            valJsonObject.put("customerId", customerId)
        }
        if (fcmDeviceId.isNotBlank()) {
            valJsonObject.put("fcmDeviceId", fcmDeviceId)
        }
        if (name.isNotBlank()) {
            valJsonObject.put("name", name)
        }
        valJsonObject.put("isSocialLogin", isSocialLogin)

        return valJsonObject.toString()
    }
}
