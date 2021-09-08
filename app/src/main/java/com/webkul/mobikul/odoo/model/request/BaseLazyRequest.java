package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.agarwal on 10/5/17.
 */

public class BaseLazyRequest {

    @SuppressWarnings("unused")
    private static final String TAG = "BaseLazyRequest";
    private static final String KEY_OFFSET = "offset";
    private static final String KEY_LIMIT = "limit";
    private final int mOffset;
    private final int mLimit;

    public BaseLazyRequest(int offset, int limit) {
        mOffset = offset;
        mLimit = limit;
    }

    @SuppressWarnings("WeakerAccess")
    public int getOffset() {
        return mOffset;
    }

    @SuppressWarnings("WeakerAccess")
    public int getLimit() {
        return mLimit;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_OFFSET, getOffset());
            jsonObject.put(KEY_LIMIT, getLimit());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
