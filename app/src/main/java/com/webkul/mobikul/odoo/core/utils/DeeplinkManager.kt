package com.webkul.mobikul.odoo.core.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.webkul.mobikul.odoo.activity.CatalogProductActivity
import com.webkul.mobikul.odoo.activity.ProductActivity
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.helper.CatalogHelper.CatalogProductRequestType
import com.webkul.mobikul.odoo.helper.OdooApplication
import javax.inject.Inject

class DeeplinkManager @Inject constructor() {


    fun handleDeeplink(sourceActivity: AppCompatActivity, intent: Intent?) {
        if (intent != null) {
            sourceActivity.startActivity(intent)
        }
    }

    fun getNotificationIntent(sourceActivity: AppCompatActivity, intent: Intent?): Intent? {
        var deeplinkIntent: Intent? = null
        try {
            when (intent?.extras?.getString(BundleConstant.BUNDLE_KEY_TYPE_V2)) {
                ApplicationConstant.TYPE_CUSTOM -> {
                    deeplinkIntent = Intent(sourceActivity, CatalogProductActivity::class.java).apply {
                        putExtra(BundleConstant.BUNDLE_KEY_SEARCH_DOMAIN, intent.extras?.getString(BundleConstant.BUNDLE_KEY_DOMAIN_V2))
                        putExtra(BundleConstant.BUNDLE_KEY_CATEGORY_NAME, intent.extras?.getString(BundleConstant.BUNDLE_KEY_NAME_V2))
                        putExtra(BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogProductRequestType.SEARCH_DOMAIN)
                    }
                }
                ApplicationConstant.TYPE_PRODUCT -> {
                    deeplinkIntent = Intent(sourceActivity, ProductActivity::class.java).apply {
                        putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_ID, intent.extras?.getString(BundleConstant.BUNDLE_KEY_ID_V2))
                        putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_NAME, intent.extras?.getString(BundleConstant.BUNDLE_KEY_NAME_V2))
                    }
                }
                ApplicationConstant.TYPE_CATEGORY -> {
                    deeplinkIntent = Intent(sourceActivity, CatalogProductActivity::class.java).apply {
                        putExtra(BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogProductRequestType.GENERAL_CATEGORY)
                        putExtra(BundleConstant.BUNDLE_KEY_CATEGORY_ID, intent.extras?.getString(BundleConstant.BUNDLE_KEY_ID_V2))
                        putExtra(BundleConstant.BUNDLE_KEY_CATEGORY_NAME, intent.extras?.getString(BundleConstant.BUNDLE_KEY_NAME_V2))
                    }
                }
                ApplicationConstant.TYPE_NONE -> {}
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return deeplinkIntent
    }

}