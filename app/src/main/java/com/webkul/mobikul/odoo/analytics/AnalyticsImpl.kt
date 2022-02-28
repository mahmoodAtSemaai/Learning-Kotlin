package com.webkul.mobikul.odoo.analytics

import android.content.Context
import com.webkul.mobikul.odoo.analytics.firebase.FirebaseAnalytics
import com.webkul.mobikul.odoo.analytics.mixpanel.MixPanelAnalytics
import com.webkul.mobikul.odoo.model.user.UserModel


object AnalyticsImpl : BaseAnalytics {
    val list = ArrayList<BaseAnalytics>()


    fun initialize(context: Context) {
        list.add(MixPanelAnalytics.getInstance(context = context))
    }


    override fun initUserTracking(userModel: UserModel) {
        list.forEach {
            it.initUserTracking(userModel)
        }
    }

    override fun trackAnyEvent(eventName: String, eventProperties: Map<String, Any?>) {
        list.forEach {
            it.trackAnyEvent(eventName, eventProperties)
        }
    }

    override fun trackAnyEvent(eventName: String) {
        trackAnyEvent(eventName, mapOf())
    }

    override fun close() {
        list.forEach {
            it.close()
        }
    }
}


// Steps to Add Tracking for any Event
// 1- Create anlytics constant
// 2- Create a function in Base Analytics
// 3- Invoke the function with correct metaData / Params