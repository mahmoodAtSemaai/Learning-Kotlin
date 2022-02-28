package com.webkul.mobikul.odoo.updates

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AlertDialog
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R

object CheckForUpdates {
    private const val alertTitle = "New Version Available"
    private const val alertDescription = "Please update to new version to continue using the app"
    private const val alertNegativeButtonTitle = "No, Thanks"
    private const val alertPositiveButtonTitle = "Update"

    @JvmStatic
    fun initUpdateChecker(context: Context) {
        if (AppUpdateHelper.isUpdateAvailable) {
            showAlertDialog(context)
        }
    }

    private fun showAlertDialog(context: Context) {
        val dialog = AlertDialog.Builder(
            ContextThemeWrapper(context, R.style.Theme_AppCompat_Light)
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
}