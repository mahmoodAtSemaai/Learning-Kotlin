package com.webkul.mobikul.odoo.model.request;

import android.content.Context;

import com.webkul.mobikul.odoo.helper.AppSharedPref;

/**
 * Created by shubham.agarwal on 8/5/17.
 */

public class ProductSliderRequest extends BaseLazyRequest {
    @SuppressWarnings("unused")
    private static final String TAG = "ProductSliderRequestDat";
    private final String mUrl;

    public ProductSliderRequest(Context context, String url, int offset) {
        super(offset, AppSharedPref.getItemsPerPage(context));
        mUrl = url;
    }

    public String getUrl() {
        if (mUrl == null) {
            return "";
        }
        return mUrl;
    }
}
