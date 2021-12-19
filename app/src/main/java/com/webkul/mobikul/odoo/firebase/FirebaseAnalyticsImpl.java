package com.webkul.mobikul.odoo.firebase;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class FirebaseAnalyticsImpl {



    private static FirebaseAnalytics mFirebaseAnalytics;

    public FirebaseAnalyticsImpl(Context context) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public static void logViewItem(Context context, String productId, String productName) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        try {
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, Integer.parseInt(productId));
        } catch (NumberFormatException e) {
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 0);
        }
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, productName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }
    public static void logEcommercePurchase(Context context, String productName) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, productName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    public static void logAppOpenEvent(Context context) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
    }

    public static void logAddToWishlistEvent(Context context, String productId, String productName) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        try {
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, Integer.parseInt(productId));
        } catch (NumberFormatException e) {
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 0);
        }
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, productName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, bundle);
    }


    public static void logAddToCartEvent(Context context, String productId, String productName) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        try {
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, Integer.parseInt(productId));
        } catch (NumberFormatException e) {
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 0);
        }
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, productName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle);

    }

    public static void logBeginCheckoutEvent(Context context) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, null);
    }


    public static void logLoginEvent(Context context, String customerId, String customerName) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, Integer.parseInt(customerId));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, customerName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }


    public static void logSearchEvent(Context context, String searchQuery) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, searchQuery);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }

    @SuppressWarnings("unused")
    public static void logShareEvent(Context context, String productId, String productName) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, Integer.parseInt(productId));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, productName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);
    }

    public static void logSignUpEvent(Context context, String customerId, String customerName) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, Integer.parseInt(customerId));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, customerName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
    }

    public static void logPostalCodeUnavailable(Context context, String state,String district,String subdistrict,String village){
        if(mFirebaseAnalytics == null){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        bundle.putString(CustomFirebaseAnalyticsEvent.UNSERVICEABLE_STATE,state);
        bundle.putString(CustomFirebaseAnalyticsEvent.UNSERVICEABLE_DISTRICT,district);
        bundle.putString(CustomFirebaseAnalyticsEvent.UNSERVICEABLE_SUB_DISTRICT,subdistrict);
        bundle.putString(CustomFirebaseAnalyticsEvent.UNSERVICEABLE_VILLAGE,village);
        mFirebaseAnalytics.logEvent(CustomFirebaseAnalyticsEvent.NON_SERVICEABLE_LOCATION_EVENT,bundle);
    }
}
