package com.webkul.mobikul.odoo.handler.extra.search;

import android.content.Context;

import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.CustomerBaseActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.ProductActivity;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

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
        }
    }
}
