package com.webkul.mobikul.odoo.core.data.local

import com.webkul.mobikul.odoo.data.entity.SplashEntity
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import com.webkul.mobikul.odoo.model.generic.ProductData
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import com.webkul.mobikul.odoo.model.notification.NotificationMessagesResponse
import javax.inject.Inject

class SaveData @Inject constructor(val sqlLiteDbHelper: SqlLiteDbHelper) {

    fun saveHomepageResponse(homePageResponse: HomePageResponse) {
        sqlLiteDbHelper.insertHomePageData(homePageResponse)
    }

    fun saveNotificationMessageResponse(notificationMessagesResponse: NotificationMessagesResponse) {
        sqlLiteDbHelper.insertNotificPageData(notificationMessagesResponse)
    }

    fun saveProductData(productData: ProductData) {
        sqlLiteDbHelper.insertProductPageData(productData)
    }

    fun saveSplashScreenResponse(splashScreenResponse: SplashScreenResponse) {
        sqlLiteDbHelper.insertSplashPageData(splashScreenResponse)
    }

    fun saveSplashEntity(splashEntity: SplashEntity) {
        sqlLiteDbHelper.insertSplashPageEntity(splashEntity)
    }

    fun saveCatalogProductResponse(catalogProductResponse: CatalogProductResponse, requestType: String) {
        sqlLiteDbHelper.insertCatalogPageData(catalogProductResponse, requestType)
    }

}