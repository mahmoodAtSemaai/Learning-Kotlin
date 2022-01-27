package com.webkul.mobikul.odoo.analytics

import android.content.Context
import com.webkul.mobikul.odoo.analytics.firebase.FirebaseAnalytics
import com.webkul.mobikul.odoo.analytics.mixpanel.MixPanelAnalytics
import com.webkul.mobikul.odoo.model.user.UserModel


object AnalyticsImpl : BaseAnalytics {
    val list = ArrayList<BaseAnalytics>()


    fun initialize(context: Context) {
        list.add(FirebaseAnalytics(context = context))
        list.add(MixPanelAnalytics.getInstance(context = context))
    }


    override fun initUserTracking(userModel: UserModel) {
        list.forEach {
            it.initUserTracking(userModel)
        }
    }

    override fun trackAnyEvent(eventName: String, value: Map<String, Any>) {
        list.forEach {
            it.trackAnyEvent(eventName, value)
        }
    }

    override fun close() {
        list.forEach {
            it.close()
        }
    }
}