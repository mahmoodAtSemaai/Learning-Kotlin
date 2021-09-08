package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 11/7/17.
 */

public class CartToWishlistRequest {

    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_LINE_ID = "line_id";

    private String mLineId;

    public CartToWishlistRequest(String lineId) {
        mLineId = lineId;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_LINE_ID, mLineId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
