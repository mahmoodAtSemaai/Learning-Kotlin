package com.webkul.mobikul.odoo.handler.product

import android.content.Context
import android.content.Intent
import com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikul.odoo.helper.OdooApplication
import com.webkul.mobikul.odoo.model.generic.ProductData

class AlternativeProductHandler(var context: Context) {

    fun clickOnProduct(productData: ProductData) {

        val intent = Intent(context, (context.getApplicationContext() as OdooApplication).productActivity)
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, productData.getTemplateId())
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, productData.getName())
        context.startActivity(intent)

    }
}