package com.webkul.mobikul.odoo.handler.customer;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.model.customer.order.OrderItem;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

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
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getProductActivity());
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
        mContext.startActivity(intent);
    }
}
