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
    String TYPE_CHAT = "chat";
    String TYPE_NONE = "none";
    String TYPE_SERVICE = "service";
    String TYPE_LOYALTY_TERMS = "open_loyalty";

    /*PAYMENT ACQUIRER CODES*/
    String ACQUIRER_CODE_STRIPE = "STRIPE";
    String ACQUIRER_CODE_COD = "COD";
    String ACQUIRER_CODE_WIRE_TRANSFER = "WIRE_TRANSFER";
    String TAG_EXTRA_INFO = "TAG_EXTRA_INFO";

    /*SEARCH */
    String MAX_SEARCH_HISTORY_RESULT = "5";
    long DEBOUNCE_REQUEST_TIMEOUT = 500;
    long REQUEST_TIMEOUT =2;

    String NOTIFICATION_CHANNEL_ABANDONED_CART = "abandonedCart";
    long DEFAULT_TIME_FOR_ABANDONED_CART_NOTIFICATION = 2*3600000;

    String BUNDLE_KEY_OPEN_CART = "BUNDLE_KEY_OPEN_CART";

    /* Product Inventory Availability Type */
    String ALWAYS = "always";
    String THRESHOLD = "threshold";
    String NEVER = "never";
    String PRE_ORDER = "pre-order";
    String CUSTOM = "custom";

//    static boolean isNetworkAvailable(final Context context) {
//        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
//        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
//    }

    /* Status Codes*/
    int SUCCESS = 200;
    int CREATED = 201;
    int NOT_FOUND = 404;

    //Time Unit
    int SECONDS_IN_A_MINUTE = 60;
    int MINUTES_IN_AN_HOUR = 60;
    int HOURS_IN_A_DAY = 24;
    int MILLIS = 1000;

    //Quantity
    int QTY_ZERO = 0;
    int MAX_ITEM_TO_BE_SHOWN_IN_CART = 10;
    int MIN_ITEM_TO_BE_SHOWN_IN_CART = 0;
    long SET_QTY_ERROR_DIALOG_TIME = 1500;
    int CART_ID_NOT_AVAILABLE = -1;

    // Point TYPE
    String CREDIT = "credit";
    String INDO_CREDIT = "Diterima";

    // Language
    String INDONESIAN = "id_ID";



    //Address
    int COMPANY_ID = 1;
    int COUNTY_ID = 100;

    //Referral Code length
    int REFERRAL_CODE_LENGTH = 6;

    //Max Address List Size
    int MAX_ADDRESS_LIST_SIZE = 5;

    //Home
    int MAX_UNREAD_CHAT_COUNT = 9;
    int MIN_UNREAD_CHAT_COUNT = 0;

    //T&C
    String MIME = "text/html";
    String ENCODING = "utf-8";
}