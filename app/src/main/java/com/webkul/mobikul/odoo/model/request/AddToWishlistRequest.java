package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 11/7/17.
 */

public class AddToWishlistRequest {

    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_PRODUCT_ID = "productId";

    private String mProductId;

    public AddToWishlistRequest(String productId) {
        mProductId = productId;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_PRODUCT_ID, mProductId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
