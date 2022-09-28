package com.webkul.mobikul.odoo.handler.product

import android.content.Context
import android.content.Intent
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.constant.BundleConstant.*
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.model.generic.ProductData
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2
import java.lang.NumberFormatException

class AlternativeProductHandler(var context: Context) {

    fun clickOnProduct(productData: ProductData) {
        AnalyticsImpl.trackProductItemSelected(
            Helper.getScreenName(context),
            productData.productId, productData.name
        )
        Intent(context, ProductActivityV2::class.java).apply {
            putExtra(BUNDLE_KEY_PRODUCT_ID, productData.productId)
            putExtra(BUNDLE_KEY_PRODUCT_NAME, productData.name)
            try {
                putExtra(BUNDLE_KEY_PRODUCT_TEMPLATE_ID, productData.templateId.toInt())
            }catch (e: NumberFormatException){}
            context.startActivity(this)
        }

    }
}