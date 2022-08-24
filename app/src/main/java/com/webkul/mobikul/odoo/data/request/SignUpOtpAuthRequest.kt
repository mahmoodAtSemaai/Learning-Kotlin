package com.webkul.mobikul.odoo.data.request

import org.json.JSONObject

data class SignUpOtpAuthRequest(
    val otp: String = "",
    val fcmToken: String = "",
    val customerId: String = "",
    val fcmDeviceId: String = "",
    val login: String = "",
    val isAuth: Boolean = true,
    val isOnboarding: Boolean = true
) {
    override fun toString(): String {
        val valJsonObject = JSONObject()
        if (otp.isNotBlank()) {
            valJsonObject.put("OTP", otp)
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
        if(login.isNotEmpty()){
            valJsonObject.put("login", login)
        }
        valJsonObject.put("isAuth", isAuth)
        valJsonObject.put("isOnboarding", isOnboarding)
        val data = JSONObject()
        data.put("data",valJsonObject)
        val params = JSONObject()
        params.put("params",data)

        return valJsonObject.toString()
    }
}
