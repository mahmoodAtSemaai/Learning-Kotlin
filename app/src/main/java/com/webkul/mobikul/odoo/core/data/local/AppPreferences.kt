package com.webkul.mobikul.odoo.core.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AppPreferences @Inject constructor(private val context: Context) {

  companion object {
    private const val CUSTOMER_PREF_NAME = "CUSTOMER_PREF"
    private const val SPLASH_PREF_NAME = "SPLASH_PREF"

    private const val MODE = Context.MODE_PRIVATE
  }

  private val customerPreferences: SharedPreferences = context.getSharedPreferences(CUSTOMER_PREF_NAME, MODE)
  private val splashPreferences: SharedPreferences =
    context.getSharedPreferences(SPLASH_PREF_NAME, MODE)

  /**
   * SharedPreferences extension function, so we won't need to call edit() and apply()
   * ourselves on every SharedPreferences operation.
   */
  private inline fun SharedPreferences.store(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
  }
}