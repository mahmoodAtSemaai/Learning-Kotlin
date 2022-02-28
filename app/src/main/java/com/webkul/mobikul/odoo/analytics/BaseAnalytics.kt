package com.webkul.mobikul.odoo.analytics

import com.webkul.mobikul.odoo.analytics.models.AnalyticsAddressDataModel
import com.webkul.mobikul.odoo.model.user.UserModel

interface BaseAnalytics {

    fun initUserTracking(userModel: UserModel)

    fun trackAnyEvent(eventName: String, eventProperties: Map<String, Any?>)

    fun trackAnyEvent(eventName: String)

    // Utility Methods

    fun trackActivityOpened(activityTitle: String) {
        val tempMap = HashMap<String, String>()
        tempMap["title"] = activityTitle
        trackAnyEvent(AnalyticsConstants.EVENT_ACTIVITYOPENED, tempMap)
    }

    fun trackActivityClosed(activityTitle: String) {
        val tempMap = HashMap<String, String>()
        tempMap["title"] = activityTitle
        trackAnyEvent(AnalyticsConstants.EVENT_ACTIVITYCLOSED, tempMap)

    }
    fun trackFragmentOpened(fragmentTitle: String) {
        val tempMap = HashMap<String, String>()
        tempMap["title"] = fragmentTitle
        trackAnyEvent(AnalyticsConstants.EVENT_FRAGMENTOPENED, tempMap)
    }

    fun trackFragmentClosed(fragmentTitle: String) {
        val tempMap = HashMap<String, String>()
        tempMap["title"] = fragmentTitle
        trackAnyEvent(AnalyticsConstants.EVENT_FRAGMENTCLOSED, tempMap)

    }

    fun trackAnalyticsFailure()
    {
        trackAnyEvent(AnalyticsConstants.EVENT_ANALYTICS_SETUP_FAILURE)
    }

    // Home Screen
    fun trackDrawerHamburgerSelected(source: String) {
        val eventMap = HashMap<String, String>()
        eventMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_DRAWER_HAMBURGER_SELECTED, eventMap)
    }

    fun trackSearchSelected(source: String) {
        val eventMap = HashMap<String, String>()
        eventMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_SEARCH_SELECTED, eventMap)
    }

    fun trackWishlistSelected(source: String) {
        val eventMap = HashMap<String, String>()
        eventMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_WISHLIST_SELECTED, eventMap)
    }

    fun trackShoppingCartSelected(source: String) {
        val eventMap = HashMap<String, String>()
        eventMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_SHOPPING_CART_SELECTED, eventMap)
    }

    fun trackPromotionalBannerSelected(id: String, name: String, source: String, position: Int) {
        val eventMap = HashMap<String, Any>()
        eventMap["id"] = id
        eventMap["name"] = name
        eventMap["source"] = source
        eventMap["position"] = position
        trackAnyEvent(AnalyticsConstants.EVENT_PROMOTIONAL_BANNER_SELECTED, eventMap)
        //todo position not being tracked right now
    }

    fun trackProductCategorySelected(categoryId: String, categoryName: String, source: String) {
        val eventMap = HashMap<String, String>()
        eventMap["categoryId"] = categoryId
        eventMap["categoryName"] = categoryName
        eventMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_PRODUCT_CATEGORY_SELECTED, eventMap)
    }

    fun trackProductItemSelected(source: String, productId: String, productName: String) {
        val eventMap = HashMap<String, String>()
        eventMap["source"] = source
        eventMap["productId"] = productId
        eventMap["productName"] = productName
        trackAnyEvent(AnalyticsConstants.EVENT_PRODUCT_ITEM_SELECTED, eventMap)
    }

    fun trackViewAllProductsSelected(source: String, categoryId: String, categoryName: String) {
        val eventMap = HashMap<String, String>()
        eventMap["source"] = source
        eventMap["categoryId"] = categoryId
        eventMap["categoryName"] = categoryName
        trackAnyEvent(AnalyticsConstants.EVENT_VIEW_ALL_PRODUCTS_SELECTED, eventMap)
    }

    // Product Screen
    fun trackItemQuantitySelected(quantity: Int, productId: String, productName: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["quantity"] = quantity
        eventMap["productId"] = productId
        eventMap["productName"] = productName
        trackAnyEvent(AnalyticsConstants.EVENT_ITEM_QUANTITY_SELECTED, eventMap)
    }

    fun trackShareButtonSelected(productId: String, productName: String) {
        val eventMap = HashMap<String, String>()
        eventMap["productId"] = productId
        eventMap["productName"] = productName
        trackAnyEvent(AnalyticsConstants.EVENT_SHARE_BUTTON_SELECTED, eventMap)
    }

    fun trackSellerProfileSelected(
        source: String,
        sellerId: String,
        sellerRating: Int,
        sellerName: String
    ) {
        val eventMap = HashMap<String, Any>()
        eventMap["source"] = source
        eventMap["sellerId"] = sellerId
        eventMap["sellerRating"] = sellerRating
        eventMap["sellerName"] = sellerName
        trackAnyEvent(AnalyticsConstants.EVENT_SELLER_PROFILE_SELECTED, eventMap)
    }

    fun trackAddItemToBagSuccessful(
        quantity: Int,
        productId: String,
        sellerId: String,
        productName: String
    ) {
        val eventMap = HashMap<String, Any>()
        eventMap["quantity"] = quantity
        eventMap["productId"] = productId
        eventMap["sellerId"] = sellerId
        eventMap["productName"] = productName
        trackAnyEvent(AnalyticsConstants.EVENT_ADD_ITEM_TO_BAG_SUCCESSFUL, eventMap)
    }

    fun trackAddItemToBagFailed(errorMessage: String, errorCode: Int, errorType: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["errorMessage"] = errorMessage
        eventMap["errorCode"] = errorCode
        eventMap["errorType"] = errorType
        trackAnyEvent(AnalyticsConstants.EVENT_ADD_ITEM_TO_BAG_FAILED, eventMap)
    }

    // Search Screen
    fun trackItemSearchSuccessful(searchTerm: String, searchResultCount: Int) {
        val eventMap = HashMap<String, Any>()
        eventMap["searchTerm"] = searchTerm
        eventMap["searchResultCount"] = searchResultCount
        trackAnyEvent(AnalyticsConstants.EVENT_ITEM_SEARCH_SUCCESSFUL, eventMap)
    }

    fun trackItemSearchFailed(
        searchTerm: String,
        errorMessage: String,
        errorCode: Int,
        errorType: String
    ) {
        val eventMap = HashMap<String, Any>()
        eventMap["searchTerm"] = searchTerm
        eventMap["errorMessage"] = errorMessage
        eventMap["errorCode"] = errorCode
        eventMap["errorType"] = errorType
        trackAnyEvent(AnalyticsConstants.EVENT_ITEM_SEARCH_FAILED, eventMap)
    }

    fun trackRelatedProductSelected(itemId: String, itemPrice: String, itemName: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemPrice"] = itemPrice
        eventMap["itemName"] = itemName
        trackAnyEvent(AnalyticsConstants.EVENT_RELATED_PRODUCT_SELECTED, eventMap)
    }

    // Wishlist Screen
    fun trackWishlistedItemSelected(itemId: String, itemName: String, itemPrice: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemName"] = itemName
        eventMap["itemPrice"] = itemPrice
        trackAnyEvent(AnalyticsConstants.EVENT_WISHLISTED_ITEM_SELECTED, eventMap)
    }

    fun trackItemRemovedFromWishlist(itemId: String, itemName: String, itemPrice: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemName"] = itemName
        eventMap["itemPrice"] = itemPrice
        trackAnyEvent(AnalyticsConstants.EVENT_ITEM_REMOVED_FROM_WISHLIST, eventMap)
    }

    fun trackItemRemoveFromWishlistFailed(errorMessage: String, errorCode: Int, errorType: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["errorMessage"] = errorMessage
        eventMap["errorCode"] = errorCode
        eventMap["errorType"] = errorType
        trackAnyEvent(AnalyticsConstants.EVENT_ITEM_REMOVE_FROM_WISHLIST_FAILED, eventMap)
    }

    fun trackMoveToBagSelected(
        itemId: String,
        itemName: String,
        itemPrice: String,
        source: String,
        sellerId: String
    ) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemName"] = itemName
        eventMap["itemPrice"] = itemPrice
        eventMap["source"] = source
        eventMap["sellerId"] = sellerId
        trackAnyEvent(AnalyticsConstants.EVENT_MOVE_TO_BAG_SELECTED, eventMap)
    }

    fun trackMoveItemToBagSuccessful(
        itemId: String,
        itemName: String,
        itemPrice: String,
        source: String,
        sellerId: String
    ) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemName"] = itemName
        eventMap["itemPrice"] = itemPrice
        eventMap["source"] = source
        eventMap["sellerId"] = sellerId
        trackAnyEvent(AnalyticsConstants.EVENT_MOVE_ITEM_TO_BAG_SUCCESSFUL, eventMap)
    }

    fun trackMoveItemToBagFailed(errorMessage: String, errorCode: Int, errorType: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["errorMessage"] = errorMessage
        eventMap["errorCode"] = errorCode
        eventMap["errorType"] = errorType
        trackAnyEvent(AnalyticsConstants.EVENT_MOVE_ITEM_TO_BAG_FAILED, eventMap)
    }

    // Bag Screen
    fun trackRemoveItemSelected(itemId: String, itemName: String, itemPrice: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemName"] = itemName
        eventMap["itemPrice"] = itemPrice
        trackAnyEvent(AnalyticsConstants.EVENT_REMOVE_ITEM_SELECTED, eventMap)
    }

    fun trackRemoveItemSuccessful(itemId: String, itemName: String, itemPrice: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemName"] = itemName
        eventMap["itemPrice"] = itemPrice
        trackAnyEvent(AnalyticsConstants.EVENT_REMOVE_ITEM_SUCCESSFUL, eventMap)
    }

    fun trackRemoveItemFailed(errorMessage: String, errorCode: Int, errorType: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["errorMessage"] = errorMessage
        eventMap["errorCode"] = errorCode
        eventMap["errorType"] = errorType
        trackAnyEvent(AnalyticsConstants.EVENT_REMOVE_ITEM_FAILED, eventMap)
    }

    fun trackEmptyShoppingBagSelected() {
        trackAnyEvent(AnalyticsConstants.EVENT_EMPTY_SHOPPING_BAG_SELECTED, HashMap())
    }

    fun trackEmptyShoppingBagSuccessful() {
        trackAnyEvent(AnalyticsConstants.EVENT_EMPTY_SHOPPING_BAG_SUCCESSFUL, HashMap())
    }

    fun trackEmptyShoppingBagFailed(errorMessage: String, errorCode: Int, errorType: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["errorMessage"] = errorMessage
        eventMap["errorCode"] = errorCode
        eventMap["errorType"] = errorType
        trackAnyEvent(AnalyticsConstants.EVENT_EMPTY_SHOPPING_BAG_FAILED, eventMap)
    }

    fun trackMoveToWishlistSelected(itemId: String, itemName: String, itemPrice: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemName"] = itemName
        eventMap["itemPrice"] = itemPrice
        trackAnyEvent(AnalyticsConstants.EVENT_MOVE_TO_WISHLIST_SELECTED, eventMap)
    }

    fun trackMoveToWishlistSuccessful(itemId: String, itemName: String, itemPrice: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemName"] = itemName
        eventMap["itemPrice"] = itemPrice
        trackAnyEvent(AnalyticsConstants.EVENT_MOVE_TO_WISHLIST_SUCCESSFUL, eventMap)
    }

    fun trackMoveToWishlistFailed(errorMessage: String, errorCode: Int, errorType: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["errorMessage"] = errorMessage
        eventMap["errorCode"] = errorCode
        eventMap["errorType"] = errorType
        trackAnyEvent(AnalyticsConstants.EVENT_MOVE_TO_WISHLIST_FAILED, eventMap)
    }

    fun trackItemQuantitySelected(itemId: String, itemName: String, oldQuantity: Int) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemName"] = itemName
        eventMap["oldQuantity"] = oldQuantity
        trackAnyEvent(AnalyticsConstants.EVENT_ITEM_QUANTITY_SELECTED, eventMap)
    }

    fun trackItemQuantityChangeSuccessful(itemId: String, itemName: String, newQuantity: Int) {
        val eventMap = HashMap<String, Any>()
        eventMap["itemId"] = itemId
        eventMap["itemName"] = itemName
        eventMap["newQuantity"] = newQuantity
        trackAnyEvent(AnalyticsConstants.EVENT_ITEM_QUANTITY_CHANGE_SUCCESSFUL, eventMap)
    }

    fun trackItemQuantityChangeFailed(errorMessage: String, errorCode: Int, errorType: String) {
        val eventMap = HashMap<String, Any>()
        eventMap["errorMessage"] = errorMessage
        eventMap["errorCode"] = errorCode
        eventMap["errorType"] = errorType
        trackAnyEvent(AnalyticsConstants.EVENT_ITEM_QUANTITY_CHANGE_FAILED, eventMap)
    }

    fun trackProceedToCheckoutSelected(
        totalAmount: String, totalTaxes: String,
        checkoutItemNameList: List<String>
    ) {
        val eventMap = HashMap<String, Any>()
        eventMap["totalAmount"] = totalAmount
        eventMap["totalTaxes"] = totalTaxes
        eventMap["checkoutItemNameList"] = checkoutItemNameList
        trackAnyEvent(AnalyticsConstants.EVENT_PROCEED_TO_CHECKOUT_SELECTED, eventMap)
    }

    // Navigation Drawer Menu Screen
    fun trackMarketPlaceClick(source: String) {
        val tempMap = HashMap<String, String>()
        tempMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_MARKET_PLACE_SELECTED, tempMap)
    }

    fun trackLanguageClick(languageCode: String) {
        val tempMap = HashMap<String, String>()
        tempMap["languageCode"] = languageCode
        trackAnyEvent(AnalyticsConstants.EVENT_LANGUAGE_SELECTED, tempMap)
    }

    fun trackLanguageChangeSuccess(languageCode: String) {
        val tempMap = HashMap<String, Any>()
        tempMap["languageCode"] = languageCode
        trackAnyEvent(AnalyticsConstants.EVENT_LANGUAGE_CHANGE_SUCCESS, tempMap)
    }

    fun trackLanguageChangeFail(
        languageCode: String,
        errorCode: Int,
        errorMessage: String
    ) {
        val tempMap = HashMap<String, Any>()
        tempMap["languageCode"] = languageCode
        tempMap["errorCode"] = errorCode
        tempMap["errorMessage"] = errorMessage
        trackAnyEvent(AnalyticsConstants.EVENT_LANGUAGE_CHANGE_FAIL, tempMap)
    }

    fun trackDynamicParentItemSelect(categoryName: String) {
        val tempMap = HashMap<String, String>()
        tempMap["categoryName"] = categoryName
        trackAnyEvent(AnalyticsConstants.EVENT_DYNAMIC_CATEGORY_SELECTED, tempMap)
    }

    fun trackSubCategoryItemSelect(
        ParentCategoryName: String,
        categoryName: String,
        categoryId: String
    ) {
        val tempMap = HashMap<String, String>()
        tempMap["ParentCategoryName"] = ParentCategoryName
        tempMap["categoryName"] = categoryName
        tempMap["categoryId"] = categoryId
        trackAnyEvent(AnalyticsConstants.EVENT_SUBCATEGORY_ITEM_SELECTED, tempMap)
    }

    // All Orders Screen
    fun trackOrderSelected(
        orderId: String,
        source: String,
        orderTotal: String,
        orderStatus: String,
        orderDate: String
    ) {
        val tempMap = HashMap<String, Any>()
        tempMap["orderId"] = orderId
        tempMap["source"] = source
        tempMap["orderTotal"] = orderTotal
        tempMap["orderStatus"] = orderStatus
        tempMap["orderDate"] = orderDate
        trackAnyEvent(AnalyticsConstants.EVENT_ORDER_SELECTED, tempMap)
    }

    // Account Screen
    fun trackDashboardSelected() {
        trackAnyEvent(AnalyticsConstants.EVENT_DASHBOARD_SELECTED)
    }

    fun trackAddressBookSelected() {
        trackAnyEvent(AnalyticsConstants.EVENT_ADDRESS_BOOK_SELECTED)
    }

    fun trackAccountInfoSelected() {
        trackAnyEvent(AnalyticsConstants.EVENT_ACCOUNT_INFO_SELECTED)
    }

    fun trackAllOrdersSelected() {
        trackAnyEvent(AnalyticsConstants.EVENT_ALL_ORDERS_SELECTED)
    }

    fun trackSignoutSuccess() {
        trackAnyEvent(AnalyticsConstants.EVENT_SIGNOUT_SUCCESS)
    }

    fun trackSignoutFailed(errorCode: Int, errorMessage: String) {
        val tempMap = HashMap<String, Any>()
        tempMap["errorCode"] = errorCode
        tempMap["errorMessage"] = errorMessage
        trackAnyEvent(AnalyticsConstants.EVENT_SIGNOUT_FAIL, tempMap)
    }

    // Notification Screen
    fun trackNotificationClicked(
        notificationId: String,
        date: String,
        banner: String,
        body: String,
        title: String
    ) {
        val tempMap = HashMap<String, Any>()
        tempMap["notificationId"] = notificationId
        tempMap["date"] = date
        tempMap["banner"] = banner
        tempMap["body"] = body
        tempMap["title"] = title
        trackAnyEvent(AnalyticsConstants.EVENT_NOTIFICATION_CLICKED, tempMap)
    }

    // Settings Screen
    fun trackShowNotificationToggleSuccess(notificationToggleState: Boolean) {
        val tempMap = HashMap<String, Any>()
        tempMap["notificationToggleState"] = notificationToggleState
        trackAnyEvent(AnalyticsConstants.EVENT_SHOW_NOTIFICATION_TOGGLE_SUCCESS, tempMap)
    }

    fun trackShowNotificationToggleFailed(errorCode: Int, errorMessage: String) {
        val tempMap = HashMap<String, Any>()
        tempMap["errorCode"] = errorCode
        tempMap["errorMessage"] = errorMessage
        trackAnyEvent(AnalyticsConstants.EVENT_SHOW_NOTIFICATION_TOGGLE_FAILED, tempMap)
    }

    fun trackShowRecentViewProductListToggleSuccess(recentlyViewedToggleState: Boolean) {
        val tempMap = HashMap<String, Any>()
        tempMap["recentlyViewedToggleState"] = recentlyViewedToggleState
        trackAnyEvent(AnalyticsConstants.EVENT_SHOW_RECENTVIEW_PRODUCT_LIST_TOGGLE_SUCCESS, tempMap)
    }

    fun trackShowRecentViewProductListToggleFailed(
        errorCode: Int,
        errorMessage: String
    ) {
        val tempMap = HashMap<String, Any>()
        tempMap["errorCode"] = errorCode
        tempMap["errorMessage"] = errorMessage
        trackAnyEvent(AnalyticsConstants.EVENT_SHOW_RECENTVIEW_PRODUCT_LIST_TOGGLE_FAILED, tempMap)
    }

    fun trackClearRecentViewProductListSuccess() {
        trackAnyEvent(AnalyticsConstants.EVENT_CLEAR_RECENTVIEW_PRODUCT_LIST_SUCCESS)
    }

    fun trackClearRecentViewProductListFailed(
        errorCode: Int,
        errorMessage: String
    ) {
        val tempMap = HashMap<String, Any>()
        tempMap["errorCode"] = errorCode
        tempMap["errorMessage"] = errorMessage
        trackAnyEvent(AnalyticsConstants.EVENT_CLEAR_RECENTVIEW_PRODUCT_LIST_FAILED, tempMap)
    }

    fun trackClearSearchHistorySuccess() {
        trackAnyEvent(AnalyticsConstants.EVENT_CLEAR_SEARCH_HISTORY_SUCCESS)
    }

    fun trackClearSearchHistoryFailed(errorCode: Int, errorMessage: String) {
        val tempMap = HashMap<String, Any>()
        tempMap["errorCode"] = errorCode
        tempMap["errorMessage"] = errorMessage
        trackAnyEvent(AnalyticsConstants.EVENT_CLEAR_SEARCH_HISTORY_FAILED, tempMap)
    }

    fun trackPrivacyPolicyClick(source: String?) {
        val tempMap = HashMap<String, Any?>()
        tempMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_PRIVACY_POLICY_SELECTED, tempMap)
    }


    /*
    LOGIN SCREEN
     */
    fun trackLoginDetailsSubmitted(loginMethod: String?, source: String? = "") {
        val tempMap = HashMap<String, Any?>()
        tempMap["loginMethod"] = loginMethod
        tempMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_LOGIN_DETAILS_SUBMITTED, tempMap)
    }

    fun trackLoginSuccessfull(loginMethod: String?, source: String? = "") {
        val tempMap = HashMap<String, Any?>()
        tempMap["loginMethod"] = loginMethod
        tempMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_LOGIN_SUCCESFUL, tempMap)
    }

    fun trackLoginFailed(errorCode: Long?, errorType: String? = "", errorMessage: String? = "") {
        val tempMap = HashMap<String, Any?>()
        tempMap["errorCode"] = errorCode
        tempMap["errorType"] = errorType
        tempMap["errorMessage"] = errorMessage
        trackAnyEvent(AnalyticsConstants.EVENT_LOGIN_FAILED, tempMap)
    }

    fun trackForgotPasswordSelected(source: String?) {
        val tempMap = HashMap<String, Any?>()
        tempMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_FORGOT_PASSWORD_SELECTED, tempMap)
    }

    /*
    FORGOT PASSWORD SCREEN
     */

    fun trackResetPasswordSelected(phoneNumber: String) {
        val tempMap = HashMap<String, String>()
        tempMap["phoneNumber"] = phoneNumber
        trackAnyEvent(AnalyticsConstants.EVENT_RESET_PASSWORD_SELECTED, tempMap)
    }

    fun trackResetPasswordRequestSuccessfull(phoneNumber: String) {
        val tempMap = HashMap<String, Any>()
        tempMap["phoneNumber"] = phoneNumber
        trackAnyEvent(AnalyticsConstants.EVENT_RESET_PASSWORD_REQUEST_SUCCESSFUL, tempMap)
    }

    fun trackResetPasswordFailed(
        errorCode: Long?,
        errorType: String? = "",
        errorMessage: String? = ""
    ) {
        val tempMap = HashMap<String, Any?>()
        tempMap["errorCode"] = errorCode
        tempMap["errorType"] = errorType
        tempMap["errorMessage"] = errorMessage
        trackAnyEvent(AnalyticsConstants.EVENT_RESET_PASSWORD_FAILED, tempMap)
    }

    /*
    SIGNUP SCREEN
     */

    fun trackSignupSelected(
        source: String?,
        signupMethod: String?,
        name: String?,
        signupDate: String?,
        isSeller: Boolean?,
        sellerState: String?
    ) {
        val tempMap = HashMap<String, Any?>()
        tempMap["source"] = source
        tempMap["signupMethod"] = signupMethod
        tempMap["name"] = name
        tempMap["signupDate"] = signupDate
        tempMap["isSeller"] = isSeller
        trackAnyEvent(AnalyticsConstants.EVENT_SIGNUP_SELECTED, tempMap)
    }

    fun trackSignupFailed(errorCode: Long?, errorType: String? = "", errorMessage: String? = "") {
        val tempMap = HashMap<String, Any?>()
        tempMap["errorCode"] = errorCode
        tempMap["errorType"] = errorType
        tempMap["errorMessage"] = errorMessage
        trackAnyEvent(AnalyticsConstants.EVENT_SIGNUP_FAILED, tempMap)
    }

    fun trackSignupSuccessfull(
        source: String?,
        signupMethod: String?,
        name: String?,
        signupDate: String?,
        isSeller: Boolean?,
        sellerState: String?
    ) {
        val tempMap = HashMap<String, Any?>()
        tempMap["source"] = source
        tempMap["signupMethod"] = signupMethod
        tempMap["name"] = name
        tempMap["signupDate"] = signupDate
        tempMap["isSeller"] = isSeller
        trackAnyEvent(AnalyticsConstants.EVENT_SIGNUP_SUCCESSFUL, tempMap)
    }

    /*
    FINGERPRINT LOGIN
     */

    fun trackUserOptsIntoFingerPrintLogin(source: String?) {
        val tempMap = HashMap<String, Any?>()
        tempMap["source"] = source
        trackAnyEvent(AnalyticsConstants.EVENT_FINGERPRINT_SELECTED, tempMap)
    }

    fun trackUserOptsIntoFingerPrintLoginSetupSuccessfull() {
        trackAnyEvent(AnalyticsConstants.EVENT_FINGERPRINT_LOGIN_SETUP_SUCCESSFUL)
    }

    fun trackUserOptsIntoFingerPrintLoginSetupFailed(
        errorCode: Int?,
        errorType: String?,
        errorMessage: String?
    ) {
        val tempMap = HashMap<String, Any?>()
        tempMap["errorCode"] = errorCode
        tempMap["errorType"] = errorType
        tempMap["errorMessage"] = errorMessage
        trackAnyEvent(AnalyticsConstants.EVENT_FINGERPRINT_LOGIN_SETUP_FAILED, tempMap)

    }

    fun trackUserOptsIntoFingerPrintLoginNotEnrolled() {
        trackAnyEvent(AnalyticsConstants.EVENT_FINGERPRINT_NOT_ENROLLED)
    }

    /*
    UPDATE ADDRESS Screen
     */

    fun trackAddressUpdateSubmitted(
        analyticsModel: AnalyticsAddressDataModel
    ) {
        val tempMap = HashMap<String, Any>()
        tempMap["district"] = analyticsModel.district
        tempMap["village"] = analyticsModel.village
        tempMap["country"] = analyticsModel.province
        trackAnyEvent(AnalyticsConstants.EVENT_ADDRESS_UPDATE_SUBMITTED, tempMap)
    }

    fun trackAddressAdded(
        analyticsModel: AnalyticsAddressDataModel
    ) {
        val tempMap = HashMap<String, Any>()
        tempMap["district"] = analyticsModel.district
        tempMap["village"] = analyticsModel.village
        tempMap["country"] = analyticsModel.province
        trackAnyEvent(AnalyticsConstants.EVENT_ADDRESS_ADDED, tempMap)
    }

    fun trackUnServicedAddress(
        missingState: String,
        missingDistrict: String,
        missingSubDistrict: String,
        missingVillage: String
    ) {
        val tempMap = HashMap<String, Any>()
        tempMap["missingState"] = missingState
        tempMap["missingDistrict"] = missingDistrict
        tempMap["missingSubDistrict"] = missingSubDistrict
        tempMap["missingVillage"] = missingVillage
        trackAnyEvent(AnalyticsConstants.EVENT_ADDRESS_UNSERVICABLE, tempMap)
    }

    fun trackAddressUpdateFailed(
        analyticsModel: AnalyticsAddressDataModel,
        errorCode: Int,
        errorType: String?,
        errorMessage: String?
    ) {
        val tempMap = HashMap<String, Any?>()
        tempMap["district"] = analyticsModel.district
        tempMap["village"] = analyticsModel.village
        tempMap["country"] = analyticsModel.province
        tempMap["errorCode"] = errorCode
        tempMap["errorType"] = errorType
        tempMap["errorMessage"] = errorMessage

        trackAnyEvent(AnalyticsConstants.EVENT_ADDRESS_UPDATE_FAILED, tempMap)

    }

    fun trackAddressUpdateSuccessfull(
        analyticsModel: AnalyticsAddressDataModel
    ) {
        val tempMap = HashMap<String, Any>()
        tempMap["district"] = analyticsModel.district
        tempMap["subDistrict"] = analyticsModel.subDistrict
        tempMap["village"] = analyticsModel.village
        tempMap["country"] = analyticsModel.province
        trackAnyEvent(AnalyticsConstants.EVENT_ADDRESS_UPDATE_SUCCESSFUL, tempMap)
    }

    fun close()

}

