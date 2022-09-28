package com.webkul.mobikul.odoo.core.utils

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.ui.routing.RoutingActivity
import java.util.*
import javax.inject.Inject

class LocaleManager @Inject constructor(private val context: Context) {

    companion object {
        const val LANG_ID = "id"
        const val LANG_COUNTRY = "ID"
        const val LANG_DELIMITER = "_"
    }

    fun setLocale(reload: Boolean, activityContext: Context? = null) {
        var langCode = AppSharedPref.getLanguageCode(context)
        if (langCode.contains(LANG_DELIMITER)) langCode = langCode.split(LANG_DELIMITER).toTypedArray()[0]
        val locale: Locale = if (langCode == LANG_ID || langCode.isBlank()) {
            Locale(LANG_ID, LANG_COUNTRY)
        } else {
            Locale(langCode)
        }
        Locale.setDefault(locale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        activityContext?.resources?.updateConfiguration(config, activityContext.resources.displayMetrics)
        if (reload) {
            Intent(context, RoutingActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        }
    }

}