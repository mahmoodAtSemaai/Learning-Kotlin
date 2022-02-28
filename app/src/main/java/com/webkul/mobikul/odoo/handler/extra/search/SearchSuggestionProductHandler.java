package com.webkul.mobikul.odoo.handler.extra.search;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.CustomerBaseActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.ProductActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.model.generic.ProductData;



/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class SearchSuggestionProductHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "SearchSuggestionProduct";
    private final Context mContext;
    private final ProductData mData;

    public SearchSuggestionProductHandler(Context context, ProductData data) {
        mContext = context;
        mData = data;
    }

    public void viewProduct() {
        AnalyticsImpl.INSTANCE.trackRelatedProductSelected(mData.getProductId(), mData.getPriceUnit(), mData.getName());
        Helper.hideKeyboard(mContext);
        if (mContext instanceof HomeActivity) {
            ((HomeActivity) mContext).mBinding.searchView.closeSearch();
        } else if (mContext instanceof CustomerBaseActivity) {
            ((CustomerBaseActivity) mContext).mBinding.searchView.closeSearch();
        } else if (mContext instanceof CatalogProductActivity) {
            ((CatalogProductActivity) mContext).mBinding.searchView.closeSearch();
        } else if (mContext instanceof ProductActivity) {
            ((ProductActivity) mContext).mBinding.searchView.closeSearch();
        }


        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getProductActivity());
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
        mContext.startActivity(intent);
    }
}
