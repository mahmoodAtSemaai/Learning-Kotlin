package com.webkul.mobikul.odoo.constant;

import android.content.Context;
import android.net.ConnectivityManager;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public interface ApplicationConstant {

    /*need to configure if the store language is different by default*/

    String[] DEFAULT_GCM_TOPICS = {"DEFAULT"};

    /*APPLICATION THAT ARE NEEDED TO CONFIGURE BUT USED IN THE APPLICATION...*/
    String SLIDER_MODE_DEFAULT = "default";
    String SLIDER_MODE_FIXED = "fixed";

    /*LOCATION API RELATED CONSTANT*/
    long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    /*TYPE CONSTANTS*/
    String TYPE_CUSTOM = "custom";
    String TYPE_PRODUCT = "product";
    String TYPE_CATEGORY = "category";
    String TYPE_NONE = "none";


    /*PAYMENT ACQUIRER CODES*/
    String ACQUIRER_CODE_STRIPE = "STRIPE";
    String ACQUIRER_CODE_COD = "COD";
    String ACQUIRER_CODE_WIRE_TRANSFER = "WIRE_TRANSFER";
    String TAG_EXTRA_INFO = "TAG_EXTRA_INFO";

    /*SEARCH */
    String MAX_SEARCH_HISTORY_RESULT = "5";
    long DEBOUNCE_REQUEST_TIMEOUT = 500;

    String NOTIFICATION_CHANNEL_ABANDONED_CART = "abandonedCart";
    long DEFAULT_TIME_FOR_ABANDONED_CART_NOTIFICATION = 2*3600000;

    String BUNDLE_KEY_OPEN_CART = "BUNDLE_KEY_OPEN_CART";

    /* Product Inventory Availability Type */
    String THRESHOLD = "threshold";

//    static boolean isNetworkAvailable(final Context context) {
//        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
//        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
//    }

}