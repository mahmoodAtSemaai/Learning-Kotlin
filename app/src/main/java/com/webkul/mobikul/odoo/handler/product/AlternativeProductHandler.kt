package com.webkul.mobikul.odoo.handler.product

import android.content.Context
import android.content.Intent
import android.util.Log
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.constant.BundleConstant.*
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.helper.OdooApplication
import com.webkul.mobikul.odoo.model.generic.ProductData

class AlternativeProductHandler(var context: Context) {

    fun clickOnProduct(productData: ProductData) {
        AnalyticsImpl.trackProductItemSelected(
            Helper.getScreenName(context),
            productData.productId, productData.name
        )
        val intent = Intent(context, (context.getApplicationContext() as OdooApplication).productActivity)
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, productData.getTemplateId())
        //intent.putExtra(BUNDLE_KEY_PRODUCT_TEMPLATE_ID, productData.getTemplateId())
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, productData.getName())
        context.startActivity(intent)

    }
}