package com.webkul.mobikul.odoo.data.request

import org.json.JSONObject

data class CustomerGroupRequest(
    var fcmToken: String = "",
    var customerId: String = "",
    var fcmDeviceId: String = "",
    var customerGrpType: String = "",
    var customerGrpName: String = "",
    var isOnboarding : Boolean = true
) {
    override fun toString(): String {
        val valJsonObject = JSONObject()
        if (fcmToken.isNotBlank()) {
            valJsonObject.put(FCM_TOKEN, fcmToken)
        }
        if (customerId.isNotBlank()) {
            valJsonObject.put(CUSTOMER_ID, customerId)
        }
        if (fcmDeviceId.isNotBlank()) {
            valJsonObject.put(FCM_DEVICE_ID, fcmDeviceId)
        }
        valJsonObject.put(CUSTOMER_GRP_ID, customerGrpType)
        valJsonObject.put(CUSTOMER_GROUP_NAME, customerGrpName)
        valJsonObject.put(IS_ONBOARDING, isOnboarding)
        val params = JSONObject()
        params.put(PARAMS,valJsonObject)
        return params.toString()
    }

    companion object {
        val FCM_TOKEN = "fcmToken"
        val CUSTOMER_ID = "customerId"
        val FCM_DEVICE_ID = "fcmDeviceId"
        val CUSTOMER_GRP_ID = "customer_grp_id"
        val CUSTOMER_GROUP_NAME = "customer_grp_name"
        val IS_ONBOARDING = "isOnboarding"
        val PARAMS = "params"
    }
}
