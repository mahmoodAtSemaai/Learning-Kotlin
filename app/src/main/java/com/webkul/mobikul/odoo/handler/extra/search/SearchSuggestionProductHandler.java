package com.webkul.mobikul.odoo.handler.extra.search;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.CustomerBaseActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.ProductActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.data.entity.ProductEntity;
import com.webkul.mobikul.odoo.helper.Helper;
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
public class SearchSuggestionProductHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "SearchSuggestionProduct";
    private final Context mContext;
    private final ProductEntity mData;

    public SearchSuggestionProductHandler(Context context, ProductEntity data) {
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
        } else if (mContext instanceof ProductActivityV2) {
            ((ProductActivityV2)mContext).getBinding().etMaterialSearchView.closeSearch();
        }


        Intent intent = new Intent(mContext, ProductActivityV2.class);
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getProductId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_TEMPLATE_ID, mData.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
        mContext.startActivity(intent);
    }
}
