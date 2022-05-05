package com.webkul.mobikul.odoo.core.extension

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Parcelable
import android.util.DisplayMetrics
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.ColorHelper
import java.io.IOException
import java.nio.charset.Charset

/**
     * Loads content of file from assets as String using UTF-8 charset
 */
fun Context.loadFromAsset(jsonName: String): String? {
    var stream: String? = null
    try {
        val inputStream = this.assets.open(jsonName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        stream = String(buffer, Charset.forName("UTF-8"))
    } catch (e: IOException) {
    }
    return stream
}

/**
 * Computes status bar height
 */
fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = this.resources.getIdentifier(
        "status_bar_height", "dimen",
        "android"
    )
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

/**
 * Convert dp integer to pixel
 */
fun Context.toPx(dp: Int): Float {
    val displayMetrics = this.resources.displayMetrics
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

/**
 * Get color from resources
 */
fun Context.getCompatColor(@ColorRes colorInt: Int): Int =
    ContextCompat.getColor(this, colorInt)

/**
 * Get drawable from resources
 */
fun Context.getCompatDrawable(@DrawableRes drawableRes: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableRes)


fun Context.getDefaultProgressDialog() : SweetAlertDialog {
    val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
    sweetAlertDialog.titleText =getString(R.string.please_wait)
    sweetAlertDialog.progressHelper.barColor = ColorHelper.getColor(this, R.attr.colorAccent)
    sweetAlertDialog.setCancelable(false)
    return sweetAlertDialog
}

fun Context.showDefaultWarningDialogWithDismissListener(
    title: String?,
    message: String?,
    listener: OnSweetClickListener?
) {
    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        .setTitleText(title)
        .setContentText(message)
        .setConfirmClickListener(listener).show()
}

fun Context.onPrivacyPolicyClick():Resource<Intent> {
    val pm: PackageManager = packageManager
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AppSharedPref.getPrivacyURL(this)))
    val intents: MutableList<Intent> = ArrayList()
    val list = pm.queryIntentActivities(intent, 0)
    for (info in list) {
        val viewIntent = Intent(intent)
        viewIntent.component = ComponentName(info.activityInfo.packageName, info.activityInfo.name)
        viewIntent.setPackage(info.activityInfo.packageName)
        intents.add(viewIntent)
    }
    for (cur in intents) {
        if (cur.component!!.className.equals(
                "com.webkul.mobikul.odoo.activity.SplashScreenActivity",
                ignoreCase = true
            )
        ) intents.remove(cur)

    }
    intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toTypedArray<Parcelable>())
    return Resource.Success(intent)
}


fun Context.showDefaultWarningDialog( title: String?, message: String?) {
    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        .setTitleText(title)
        .setContentText(message)
        .setConfirmClickListener { obj: SweetAlertDialog -> obj.dismiss() }.show()
}