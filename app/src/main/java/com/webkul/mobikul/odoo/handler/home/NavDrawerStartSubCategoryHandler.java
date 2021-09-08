package com.webkul.mobikul.odoo.handler.home;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.model.generic.CategoryData;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;

/**
 * Created by shubham.agarwal on 27/3/17.
 */

public class NavDrawerStartSubCategoryHandler {
    private final Context mContext;
    private final CategoryData mData;

    public NavDrawerStartSubCategoryHandler(Context context, CategoryData childCategoryData) {
        mContext = context;
        mData = childCategoryData;
    }

    public void viewCategory() {
        Intent intent = new Intent(mContext, CatalogProductActivity.class);
        intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.GENERAL_CATEGORY);
        intent.putExtra(BUNDLE_KEY_CATEGORY_ID, mData.getCategoryId());
        intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, mData.getName());
        mContext.startActivity(intent);
        ((HomeActivity) mContext).mBinding.drawerLayout.closeDrawers();
    }

}
