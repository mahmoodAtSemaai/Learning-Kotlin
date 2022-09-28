package com.webkul.mobikul.odoo.handler.customer;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.model.customer.order.OrderItem;
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2;

/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */


public class OrderProductInfoItemHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderProductInfoItemHan";
    private final Context mContext;
    private final OrderItem mData;

    public OrderProductInfoItemHandler(Context context, OrderItem orderItem) {

        mContext = context;
        mData = orderItem;
    }

    public void viewProduct() {
        Intent intent = new Intent(mContext, ProductActivityV2.class);
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getProductId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
        try {intent.putExtra(BUNDLE_KEY_PRODUCT_TEMPLATE_ID, Integer.parseInt(mData.getTemplateId()));
        } catch(NumberFormatException ignored){}
        mContext.startActivity(intent);
    }
}
