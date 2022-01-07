package com.webkul.mobikul.odoo.analytics

import com.webkul.mobikul.odoo.model.user.UserModel

interface BaseAnalytics {

    fun initUserTracking(userModel: UserModel)

    fun trackAnyEvent(eventName: String, value: Map<String, Any>)

    fun trackPurchase(email: String, purchaseID: String) {
        val tempMap = HashMap<String, String>()
        tempMap["email"] = email
        tempMap["purchaseID"] = purchaseID
        trackAnyEvent(AnalyticsConstants.EVENT_PURCHASE, tempMap)
    }

    fun close()

}