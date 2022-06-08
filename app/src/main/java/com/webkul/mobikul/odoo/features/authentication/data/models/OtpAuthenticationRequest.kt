package com.webkul.mobikul.odoo.features.authentication.data.models

import org.json.JSONObject

data class OtpAuthenticationRequest(
    val otp: String = "",
    val fcmToken: String = "",
    val customerId: String = "",
    val fcmDeviceId: String = ""
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
        val data = JSONObject()
        data.put("data",valJsonObject)
        val params = JSONObject()
        params.put("params",data)

        return params.toString()
    }
}
