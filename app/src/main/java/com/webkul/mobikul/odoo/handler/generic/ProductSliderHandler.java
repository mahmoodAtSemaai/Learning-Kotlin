package com.webkul.mobikul.odoo.handler.generic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.snackbar.Snackbar;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_SLIDER_ID;

/**
 * Created by shubham.agarwal on 8/5/17.
 */

public class ProductSliderHandler {

    @SuppressWarnings("unused")
    private static final String TAG = "ProductSliderHandler";
    private Context mContext;

    public ProductSliderHandler(Context context) {
        mContext = context;
    }

    public void viewAll(int sliderId, String title) {
        AnalyticsImpl.INSTANCE.trackViewAllProductsSelected(Helper.getScreenName(mContext), "", title);
        Intent intent = new Intent(mContext, CatalogProductActivity.class);
        intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.PRODUCT_SLIDER);
        intent.putExtra(BUNDLE_KEY_SLIDER_ID, sliderId);
        intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, title);
        mContext.startActivity(intent);
    }
}
