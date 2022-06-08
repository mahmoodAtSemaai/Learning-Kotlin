package com.webkul.mobikul.odoo.core.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.webkul.mobikul.odoo.core.extension.getCompatDrawable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class ResourcesProvider @Inject constructor(private val context: Context) {

    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    fun getDrawabale(@DrawableRes drawableResId: Int): Drawable {
        return context.getCompatDrawable(drawableResId) ?: ColorDrawable(Color.TRANSPARENT)
    }
}