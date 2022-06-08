package com.webkul.mobikul.odoo.core.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AppPreferences @Inject constructor(private val context: Context) {

    companion object {
        private const val CUSTOMER_PREF_NAME = "CUSTOMER_PREF"
        private const val SPLASH_PREF_NAME = "SPLASH_PREF"

        private const val MODE = Context.MODE_PRIVATE


        private const val KEY_CUSTOMER_LOGIN_BASE_64_STR = "CUSTOMER_LOGIN_BASE_64_STR"
        private const val KEY_CUSTOMER_IS_SOCIAL_LOGGED_IN = "CUSTOMER_IS_SOCIAL_LOGGED_IN"
        private const val KEY_CUSTOMER_JWT_AUTH_TOKEN = "CUSTOMER_JWT_AUTH_TOKEN"
        private const val KEY_LANGUAGE_CODE =
            "language_code" //Keeping Consistent with previous preference value
        private const val KEY_CUSTOMER_ID = "CUSTOMER_ID"
        private const val KEY_IS_APP_RUN_FIRST_TIME = "KEY_IS_APP_RUN_FIRST_TIME"


        private const val PRIVACY_POLICY_URL = "PRIVACY_POLICY_URL"

    }

    private val customerPreferences: SharedPreferences =
        context.getSharedPreferences(CUSTOMER_PREF_NAME, MODE)
    private val splashPreferences: SharedPreferences =
        context.getSharedPreferences(SPLASH_PREF_NAME, MODE)

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    val isLoggedIn: Boolean
        get() {
            val value = customerPreferences.getString(KEY_CUSTOMER_LOGIN_BASE_64_STR, "")
            return !value.isNullOrEmpty() || !authToken.isNullOrEmpty()
        }

    val isSocialLoggedIn: Boolean
        get() {
            return customerPreferences.getBoolean(KEY_CUSTOMER_IS_SOCIAL_LOGGED_IN, false)
        }

    var languageCode: String?
        get() {
            return splashPreferences.getString(KEY_LANGUAGE_CODE, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_LANGUAGE_CODE, value)
        }

    var customerLoginToken: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_LOGIN_BASE_64_STR, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_CUSTOMER_LOGIN_BASE_64_STR, value)
        }

    var privacyUrl: String?
        get() {
            return splashPreferences.getString(PRIVACY_POLICY_URL, "")
        }
        set(value) = splashPreferences.edit {
            it.putString(PRIVACY_POLICY_URL, value)
        }

    var authToken: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_JWT_AUTH_TOKEN, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_CUSTOMER_JWT_AUTH_TOKEN, value)
        }

    var customerId: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_ID, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_CUSTOMER_ID, value)
        }

    var isFirstTime: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_APP_RUN_FIRST_TIME, true)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_APP_RUN_FIRST_TIME, value)
        }

}