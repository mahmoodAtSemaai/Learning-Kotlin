package com.webkul.mobikul.odoo.model.request;

import com.webkul.mobikul.odoo.helper.CatalogHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.agarwal on 19/5/17.
 */

public class SearchRequest extends BaseLazyRequest {
    @SuppressWarnings("unused")
    private static final String TAG = "SearchRequest";
    private static final String KEY_SEARCH = "search";
    private static final String KEY_DOMAIN = "domain";
    private final String mSearchKeyword;
    private final CatalogHelper.CatalogProductRequestType mCatalogProductRequestType;

    public SearchRequest(String searchKeyword, int offset, int limit, CatalogHelper.CatalogProductRequestType catalogProductRequestType) {
        super(offset, limit);
        mSearchKeyword = searchKeyword;
        mCatalogProductRequestType = catalogProductRequestType;
    }

    @SuppressWarnings("WeakerAccess")
    public String getSearchKeyword() {
        if (mSearchKeyword == null) {
            return "";
        }
        return mSearchKeyword;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(super.toString());
            jsonObject.put(mCatalogProductRequestType == CatalogHelper.CatalogProductRequestType.SEARCH_DOMAIN ? KEY_DOMAIN : KEY_SEARCH, getSearchKeyword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
