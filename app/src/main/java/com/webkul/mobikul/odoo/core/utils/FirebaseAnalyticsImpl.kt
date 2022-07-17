package com.webkul.mobikul.odoo.core.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.webkul.mobikul.odoo.firebase.CustomFirebaseAnalyticsEvent
import javax.inject.Inject

class FirebaseAnalyticsImpl @Inject constructor(private val context: Context) {

    fun logViewItem(productId: String, productName: String?) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, Bundle().apply {
            try {
                putInt(FirebaseAnalytics.Param.ITEM_ID, productId.toInt())
            } catch (e: NumberFormatException) {
                putInt(FirebaseAnalytics.Param.ITEM_ID, 0)
            }
            putString(FirebaseAnalytics.Param.ITEM_NAME, productName)
        })
    }

    fun logEcommercePurchase(productName: String?) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_NAME, productName)
        })
    }

    fun logAppOpenEvent() = FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.APP_OPEN, null)

    fun logAddToWishlistEvent(productId: String, productName: String?) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, Bundle().apply {
            try {
                putInt(FirebaseAnalytics.Param.ITEM_ID, productId.toInt())
            } catch (e: NumberFormatException) {
                putInt(FirebaseAnalytics.Param.ITEM_ID, 0)
            }
            putString(FirebaseAnalytics.Param.ITEM_NAME, productName ?: "")
        })
    }

    fun logAddToCartEvent(productId: String, productName: String?) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.ADD_TO_CART, Bundle().apply {
            try {
                putInt(FirebaseAnalytics.Param.ITEM_ID, productId.toInt())
            } catch (e: NumberFormatException) {
                putInt(FirebaseAnalytics.Param.ITEM_ID, 0)
            }
            putString(FirebaseAnalytics.Param.ITEM_NAME, productName ?: "")
        })
    }

    fun logBeginCheckoutEvent() = FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, null)

    fun logLoginEvent(customerId: String, customerName: String?) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.LOGIN, Bundle().apply {
            putInt(FirebaseAnalytics.Param.ITEM_ID, customerId.toInt())
            putString(FirebaseAnalytics.Param.ITEM_NAME, customerName ?: "")
        })
    }

    fun logSearchEvent(searchQuery: String?) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SEARCH, Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_NAME, searchQuery ?: "")
        })
    }

    fun logShareEvent(productId: String, productName: String?) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SHARE, Bundle().apply {
            putInt(FirebaseAnalytics.Param.ITEM_ID, productId.toInt())
            putString(FirebaseAnalytics.Param.ITEM_NAME, productName ?: "")
        })
    }

    fun logSignUpEvent(customerId: String, customerName: String?) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SIGN_UP, Bundle().apply {
            putInt(FirebaseAnalytics.Param.ITEM_ID, customerId.toInt())
            putString(FirebaseAnalytics.Param.ITEM_NAME, customerName ?: "")
        })
    }

    fun logPostalCodeUnavailable(
            state: String?,
            district: String?,
            subdistrict: String?,
            village: String?
    ) {
        FirebaseAnalytics.getInstance(context).logEvent(
                CustomFirebaseAnalyticsEvent.NON_SERVICEABLE_LOCATION_EVENT,
                Bundle().apply {
                    putString(CustomFirebaseAnalyticsEvent.UNSERVICEABLE_STATE, state ?: "")
                    putString(CustomFirebaseAnalyticsEvent.UNSERVICEABLE_DISTRICT, district ?: "")
                    putString(CustomFirebaseAnalyticsEvent.UNSERVICEABLE_SUB_DISTRICT, subdistrict
                            ?: "")
                    putString(CustomFirebaseAnalyticsEvent.UNSERVICEABLE_VILLAGE, village ?: "")
                })
    }
}