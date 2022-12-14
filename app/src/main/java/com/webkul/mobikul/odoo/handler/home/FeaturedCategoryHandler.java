package com.webkul.mobikul.odoo.handler.home;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.generic.FeaturedCategoryData;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;

/**
 * Created by Shubham Agarwal on 6/1/17. @Webkul Software Private limited
 */

public class FeaturedCategoryHandler {

    private final Context mContext;
    private final FeaturedCategoryData mFeaturedCategotyData;

    public FeaturedCategoryHandler(Context context, FeaturedCategoryData featuredCategotyData) {
        mContext = context;
        mFeaturedCategotyData = featuredCategotyData;
    }

    public void viewCategory() {}
}
