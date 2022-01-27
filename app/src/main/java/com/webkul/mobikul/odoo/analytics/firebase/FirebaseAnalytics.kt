package com.webkul.mobikul.odoo.analytics.firebase

import android.content.Context
import android.os.Bundle
import com.webkul.mobikul.odoo.analytics.BaseAnalytics
import com.webkul.mobikul.odoo.model.user.UserModel

class FirebaseAnalytics(context: Context) : BaseAnalytics {
    private val fireAnalytics = com.google.firebase.analytics.FirebaseAnalytics.getInstance(context)
    override fun initUserTracking(userModel: UserModel) {
        fireAnalytics.setUserId(userModel.analyticsId)
    }

    override fun trackAnyEvent(eventName: String, value: Map<String, Any>) {
        val bundle = Bundle()
        value.forEach {
            bundle.putString(it.key, it.value.toString())
        }
        fireAnalytics.logEvent(eventName, bundle)
    }

    override fun close() {
        fireAnalytics.setUserId(null)
    }
}