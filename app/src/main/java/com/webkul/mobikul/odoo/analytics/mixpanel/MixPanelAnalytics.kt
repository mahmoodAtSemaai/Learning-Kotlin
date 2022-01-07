package com.webkul.mobikul.odoo.analytics.mixpanel

import android.annotation.SuppressLint
import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.analytics.BaseAnalytics
import com.webkul.mobikul.odoo.model.user.UserModel

class MixPanelAnalytics(val context: Context) : BaseAnalytics {
    private val mixpanel: MixpanelAPI = MixpanelAPI.getInstance(context, BuildConfig.MIXPANEL_TOKEN)


    override fun initUserTracking(userModel: UserModel) {

        mixpanel.identify(userModel.analyticsId)
        mixpanel.people.identify(userModel.analyticsId)
        mixpanel.people.apply {
            set("name", userModel.name)
            set("email", userModel.email)
            set("isSeller", userModel.isSeller)
        }


    }

    override fun trackAnyEvent(eventName: String, value: Map<String, Any>) {
        mixpanel.trackMap(eventName, value)
    }

    override fun close() {
        mixpanel.flush()
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var INSTANCE: MixPanelAnalytics
        fun getInstance(context: Context): MixPanelAnalytics {
            if (!::INSTANCE.isInitialized)
                INSTANCE = MixPanelAnalytics(context)
            return INSTANCE
        }
    }

}