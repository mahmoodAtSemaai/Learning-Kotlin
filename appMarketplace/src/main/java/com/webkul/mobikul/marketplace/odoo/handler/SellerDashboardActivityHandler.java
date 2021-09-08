package com.webkul.mobikul.marketplace.odoo.handler;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.marketplace.odoo.activity.SellerOrdersListActivity;
import com.webkul.mobikul.marketplace.odoo.activity.SellerProductsListActivity;

import static com.webkul.mobikul.marketplace.odoo.constant.MarketplaceBundleConstant.BUNDLE_KEY_ORDER_TYPE;
import static com.webkul.mobikul.marketplace.odoo.constant.MarketplaceBundleConstant.BUNDLE_KEY_PRODUCT_TYPE;

/**
 * Created by aastha.gupta on 22/2/18 in Mobikul_Odoo_Application.
 */

public class SellerDashboardActivityHandler {

    private final Context mContext;

    public SellerDashboardActivityHandler(Context mContext) {
        this.mContext = mContext;
    }

    public void onClickPendingProducts() {
        Intent intent = new Intent(mContext, SellerProductsListActivity.class);
        intent.putExtra(BUNDLE_KEY_PRODUCT_TYPE, "pending");
        mContext.startActivity(intent);
    }

    public void onClickApprovedProducts() {
        Intent intent = new Intent(mContext, SellerProductsListActivity.class);
        intent.putExtra(BUNDLE_KEY_PRODUCT_TYPE, "approved");
        mContext.startActivity(intent);
    }

    public void onClickRejectedProducts() {
        Intent intent = new Intent(mContext, SellerProductsListActivity.class);
        intent.putExtra(BUNDLE_KEY_PRODUCT_TYPE, "rejected");
        mContext.startActivity(intent);
    }

    public void onClickNewOrders() {
        Intent intent = new Intent(mContext, SellerOrdersListActivity.class);
        intent.putExtra(BUNDLE_KEY_ORDER_TYPE, "new");
        mContext.startActivity(intent);
    }

    public void onClickApprovedOrders() {
        Intent intent = new Intent(mContext, SellerOrdersListActivity.class);
        intent.putExtra(BUNDLE_KEY_ORDER_TYPE, "approved");
        mContext.startActivity(intent);
    }

    public void onClickShppedOrders() {
        Intent intent = new Intent(mContext, SellerOrdersListActivity.class);
        intent.putExtra(BUNDLE_KEY_ORDER_TYPE, "shipped");
        mContext.startActivity(intent);
    }

}
