package com.webkul.mobikul.odoo.analytics

object AnalyticsConstants {

    const val EVENT_ANALYTICS_SETUP_FAILURE: String="Analytics Setup Failure"

    // Activity Tracking
    const val EVENT_ACTIVITYOPENED: String = "Activity Opened"
    const val EVENT_ACTIVITYCLOSED: String = "Activity Closed"
    // Home Screen
    const val EVENT_DRAWER_HAMBURGER_SELECTED = "Drawer Hamburger Selected"
    const val EVENT_SEARCH_SELECTED = "Search Selected"
    const val EVENT_WISHLIST_SELECTED = "Wishlist Selected"
    const val EVENT_SHOPPING_CART_SELECTED = "Shopping Cart Selected"
    const val EVENT_PROMOTIONAL_BANNER_SELECTED = "Promotional Banner Selected"
    const val EVENT_PRODUCT_CATEGORY_SELECTED = "Product Category Selected"
    const val EVENT_PRODUCT_ITEM_SELECTED = "Product Item Selected"
    const val EVENT_VIEW_ALL_PRODUCTS_SELECTED = "View All Products Selected"

    // Product Screen
    const val EVENT_ITEM_QUANTITY_SELECTED = "Item Quantity Selected"
    const val EVENT_SHARE_BUTTON_SELECTED = "Share Button Selected"
    const val EVENT_SELLER_PROFILE_SELECTED = "Seller Profile Selected"
    const val EVENT_ADD_ITEM_TO_BAG_SUCCESSFUL = "Add Item To Bag Successful"
    const val EVENT_ADD_ITEM_TO_BAG_FAILED = "Add Item To Bag Failed"

    // Search Screen
    const val EVENT_ITEM_SEARCH_SUCCESSFUL = "Item Search Successful"
    const val EVENT_ITEM_SEARCH_FAILED = "Item Search Failed"
    const val EVENT_RELATED_PRODUCT_SELECTED = "Related Product Selected"

    // Wishlist Screen
    const val EVENT_WISHLISTED_ITEM_SELECTED = "Wishlisted Item Selected"
    const val EVENT_ITEM_REMOVED_FROM_WISHLIST =
        "Item Removed from Wishlist" // modify as success event
    const val EVENT_ITEM_REMOVE_FROM_WISHLIST_FAILED =
        "Item Remove from Wishlist Failed" // failure event
    const val EVENT_MOVE_TO_BAG_SELECTED = "Move To Bag Selected"
    const val EVENT_MOVE_ITEM_TO_BAG_SUCCESSFUL = "Move Item To Bag Successful"
    const val EVENT_MOVE_ITEM_TO_BAG_FAILED = "Move Item To Bag Failed"

    // Bag Screen
    const val EVENT_REMOVE_ITEM_SELECTED = "Remove Item Selected"
    const val EVENT_REMOVE_ITEM_SUCCESSFUL = "Remove Item Successful"
    const val EVENT_REMOVE_ITEM_FAILED = "Remove Item Failed"
    const val EVENT_EMPTY_SHOPPING_BAG_SELECTED = "Empty Shopping Bag Selected"
    const val EVENT_EMPTY_SHOPPING_BAG_SUCCESSFUL = "Empty Shopping Bag Successful"
    const val EVENT_EMPTY_SHOPPING_BAG_FAILED = "Empty Shopping Bag Failed"
    const val EVENT_MOVE_TO_WISHLIST_SELECTED = "Move To Wishlist Selected"
    const val EVENT_MOVE_TO_WISHLIST_SUCCESSFUL = "Move To Wishlist Successful"
    const val EVENT_MOVE_TO_WISHLIST_FAILED = "Move To Wishlist Failed"
    const val EVENT_ITEM_QUANTITY_CHANGE_SUCCESSFUL = "Item Quantity Change Successful"
    const val EVENT_ITEM_QUANTITY_CHANGE_FAILED = "Item Quantity Change Failed"
    const val EVENT_PROCEED_TO_CHECKOUT_SELECTED = "Proceed To Checkout Selected"

    // Navigation Drawer Menu Screen
    const val EVENT_MARKET_PLACE_SELECTED: String = "Market Place Selected"
    const val EVENT_LANGUAGE_SELECTED: String = "Language Change Selected"
    const val EVENT_LANGUAGE_CHANGE_SUCCESS: String = "Language Change Successful"
    const val EVENT_LANGUAGE_CHANGE_FAIL: String = "Language Change Failed"
    const val EVENT_DYNAMIC_CATEGORY_SELECTED: String = "Dynamic Category Item Selected"
    const val EVENT_SUBCATEGORY_ITEM_SELECTED: String = "SubCategory Item Selected"

    // All Order Screen
    const val EVENT_ORDER_SELECTED: String = "Order Selected"

    // Account Screen
    const val EVENT_DASHBOARD_SELECTED: String = "Dashboard Selected"
    const val EVENT_ADDRESS_BOOK_SELECTED: String = "Address Book Selected"
    const val EVENT_ACCOUNT_INFO_SELECTED: String = "Account Info Selected"
    const val EVENT_ALL_ORDERS_SELECTED: String = "All Orders Selected"
    const val EVENT_SIGNOUT_FAIL: String = "Signout Failed"
    const val EVENT_SIGNOUT_SUCCESS: String = "Signout Successful"

    // Notification Screen
    const val EVENT_NOTIFICATION_CLICKED: String = "Notification Clicked"

    // Setting Screen
    const val EVENT_SHOW_NOTIFICATION_TOGGLE_SUCCESS: String = "Show Notification toggle Successful"
    const val EVENT_SHOW_NOTIFICATION_TOGGLE_FAILED: String = "Show Notification toggle Failed"
    const val EVENT_SHOW_RECENTVIEW_PRODUCT_LIST_TOGGLE_SUCCESS: String =
        "Show RecentView Product List toggle Successful"
    const val EVENT_SHOW_RECENTVIEW_PRODUCT_LIST_TOGGLE_FAILED: String =
        "Show RecentView Product List toggle Failed"
    const val EVENT_CLEAR_RECENTVIEW_PRODUCT_LIST_SUCCESS: String =
        "Clear RecentView Product List Successful"
    const val EVENT_CLEAR_RECENTVIEW_PRODUCT_LIST_FAILED: String =
        "Clear RecentView Product List Failed"
    const val EVENT_CLEAR_SEARCH_HISTORY_SUCCESS: String = "Clear Search History Successful"
    const val EVENT_CLEAR_SEARCH_HISTORY_FAILED: String = "Clear Search History Failed"
    const val EVENT_PRIVACY_POLICY_SELECTED: String = "Privacy Policy Selected"

    // Login Screen
    const val EVENT_LOGIN_DETAILS_SUBMITTED: String = "Login Details Submitted"
    const val EVENT_LOGIN_SUCCESFUL: String = "Login Successful"
    const val EVENT_FORGOT_PASSWORD_SELECTED: String = "Forgot Password Selected"
    const val EVENT_LOGIN_FAILED: String = "Login Failed"
    const val EVENT_RESET_PASSWORD_SELECTED: String = "Reset Password Selected"
    // Password Reset Screen

    const val EVENT_RESET_PASSWORD_FAILED: String = "Reset Password Failed"
    const val EVENT_RESET_PASSWORD_REQUEST_SUCCESSFUL: String = "Reset Password Request Successful"

    // Signup Screen
    const val EVENT_SIGNUP_FAILED: String = "Signup Failed"
    const val EVENT_SIGNUP_SELECTED: String = "Signup Selected"
    const val EVENT_SIGNUP_SUCCESSFUL: String = "Signup Successful"

    // FingerPrint Screen
    const val EVENT_FINGERPRINT_SELECTED: String = "Fingerprint Selected"
    const val EVENT_FINGERPRINT_LOGIN_SETUP_SUCCESSFUL: String = "Fingerprint Login Setup Successful"
    const val EVENT_FINGERPRINT_LOGIN_SETUP_FAILED: String = "Fingerprint Login Setup Failed"
    const val EVENT_FINGERPRINT_NOT_ENROLLED: String = "Fingerprint Not Enrolled"

    // Address Update Screen
    const val EVENT_ADDRESS_UPDATE_FAILED: String = "Address Update Failed"
    const val EVENT_ADDRESS_UPDATE_SUBMITTED: String = "Address Update Submitted"
    const val EVENT_ADDRESS_ADDED: String = "Address Added"
    const val EVENT_ADDRESS_UPDATE_SUCCESSFUL: String = "Address Update Successful"
    const val EVENT_ADDRESS_UNSERVICABLE: String = "Address Unservicable"
}