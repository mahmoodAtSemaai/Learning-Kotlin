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
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_URL;

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

    public void viewAll(String url, String title) {
        if (url.isEmpty()) {
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.error_cant_perform_operation), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
            return;
        }
        AnalyticsImpl.INSTANCE.trackViewAllProductsSelected(Helper.getScreenName(mContext), "", title);
        Intent intent = new Intent(mContext, CatalogProductActivity.class);
        intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.PRODUCT_SLIDER);
        intent.putExtra(BUNDLE_KEY_URL, url);
        intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, title);
        mContext.startActivity(intent);
//
    }
}
