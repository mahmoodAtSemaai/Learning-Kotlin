package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.agarwal on 19/5/17.
 */

public class CategoryRequest extends BaseLazyRequest {
    @SuppressWarnings("unused")
    private static final String TAG = "CategoryRequest";
    private static final String KEY_CATEGORY_ID = "cid";
    private final String mCategoryId;

    public CategoryRequest(String categoryId, int offset, int limit) {
        super(offset, limit);
        mCategoryId = categoryId;
    }


    public String getCategoryId() {
        if (mCategoryId == null) {
            return "";
        }
        return mCategoryId;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(super.toString());
            jsonObject.put(KEY_CATEGORY_ID, getCategoryId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
