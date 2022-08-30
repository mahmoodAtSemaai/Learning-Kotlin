package com.webkul.mobikul.odoo.handler.extra.search;

import android.content.Context;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.CustomerBaseActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.NewHomeActivity;
import com.webkul.mobikul.odoo.activity.ProductActivity;
import com.webkul.mobikul.odoo.activity.ProductActivityV1;
import com.webkul.mobikul.odoo.custom.MaterialSearchView;


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

public class SearchHistoryHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "SearchHistoryHandler";
    private Context mContext;
    private String mSearchQuery;

    public SearchHistoryHandler(Context context, String searchQuery) {
        mContext = context;
        mSearchQuery = searchQuery;
    }

    public void onQuerySelected() {
        if (mContext instanceof HomeActivity) {
            ((HomeActivity) mContext).mBinding.searchView.setQuery(mSearchQuery, false);
        } else if (mContext instanceof CustomerBaseActivity) {
            ((CustomerBaseActivity) mContext).mBinding.searchView.setQuery(mSearchQuery, false);
        } else if (mContext instanceof CatalogProductActivity) {
            ((CatalogProductActivity) mContext).mBinding.searchView.setQuery(mSearchQuery, false);
        } else if (mContext instanceof ProductActivity) {
            ((ProductActivity) mContext).mBinding.searchView.setQuery(mSearchQuery, false);
        } else if (mContext instanceof NewHomeActivity) {
            ((NewHomeActivity) mContext).binding.materialSearchView.setQuery(mSearchQuery, false);
        } else if (mContext instanceof ProductActivityV1) {
            ((ProductActivityV1) mContext).binding.etMaterialSearchView.setQuery(mSearchQuery, false);
        } else if (mContext instanceof BaseActivity) {
            MaterialSearchView materialSearchView = ((BaseActivity) mContext).findViewById(R.id.search_view);
            if (materialSearchView != null) {
                materialSearchView.setQuery(mSearchQuery, false);
            }
        }
    }
}