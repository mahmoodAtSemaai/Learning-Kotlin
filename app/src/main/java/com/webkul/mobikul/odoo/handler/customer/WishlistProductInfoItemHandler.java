package com.webkul.mobikul.odoo.handler.customer;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.data.response.models.WishListData;
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2;

public class WishlistProductInfoItemHandler {
    private final Context mContext;
    private final WishListData mData;

    public WishlistProductInfoItemHandler(Context context, WishListData orderItem) {
        mContext = context;
        mData = orderItem;
    }

    public void viewProduct() {
        AnalyticsImpl.INSTANCE.trackWishlistedItemSelected(String.valueOf(mData.getWishlistId()), mData.getName(), mData.getPriceUnit());
        Intent intent = new Intent(mContext, ProductActivityV2.class);
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, String.valueOf(mData.getProductId())); //converted ProductId to string
        intent.putExtra(BUNDLE_KEY_PRODUCT_TEMPLATE_ID,mData.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
        mContext.startActivity(intent);
    }


}
