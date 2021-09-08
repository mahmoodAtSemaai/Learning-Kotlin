package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 10/7/17.
 */

public class WishListToCartRequest {

    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_WISHLIST_ID = "wishlistId";

    private String wishlistId;
    private int mQty = 1;

    public WishListToCartRequest(String wishlistId) {
        this.wishlistId = wishlistId;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_WISHLIST_ID, wishlistId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
