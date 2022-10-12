package com.webkul.mobikul.odoo.core.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.webkul.mobikul.odoo.core.extension.getCompatDrawable
import javax.inject.Inject

class ResourcesProvider @Inject constructor(private val context: Context) {

    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    fun getDrawable(@DrawableRes drawableResId: Int): Drawable {
        return context.getCompatDrawable(drawableResId) ?: ColorDrawable(Color.TRANSPARENT)
    }

    fun getDrawableName(@DrawableRes drawableResId: Int): String {
        return context.resources.getResourceName(drawableResId)
    }

    fun getDisplayMetrics(): DisplayMetrics {
        return context.resources.displayMetrics
    }
}