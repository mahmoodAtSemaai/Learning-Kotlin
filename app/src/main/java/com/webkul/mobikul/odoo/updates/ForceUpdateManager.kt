package com.webkul.mobikul.odoo.updates

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AlertDialog
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R

object ForceUpdateManager {
    private lateinit var alertTitle: String
    private lateinit var alertDescription: String
    private lateinit var alertNegativeButtonTitle: String
    private lateinit var alertPositiveButtonTitle: String

    @JvmStatic
    fun init(context: Context) {
        if (FirebaseRemoteConfigHelper.isUpdateAvailable) {
            setStringValues(context)
            showAlertDialog(context)
        }
    }

    private fun showAlertDialog(context: Context) {
        val dialog = AlertDialog.Builder(
            ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_NoActionBar)
        )
            .setTitle(alertTitle)
            .setMessage(alertDescription)
            .setPositiveButton(
                alertPositiveButtonTitle
            ) { _, _ -> redirectToPlayStore(context)
                (context as Activity).finish() }
            .setNegativeButton(
                alertNegativeButtonTitle
            ) { _, _ -> (context as Activity).finish() }
            .setCancelable(false)
            .create()
        dialog.show()
    }

    private fun redirectToPlayStore(context: Context) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.APP_PLAYSTORE_URL))
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
    }

    private fun setStringValues(context: Context) {
        alertTitle = context.getString(R.string.app_update_alert_title)
        alertDescription = context.getString(R.string.app_update_alert_description)
        alertNegativeButtonTitle = context.getString(R.string.app_update_alert_negative_button_title)
        alertPositiveButtonTitle = context.getString(R.string.app_update_alert_positive_button_title)
    }
}