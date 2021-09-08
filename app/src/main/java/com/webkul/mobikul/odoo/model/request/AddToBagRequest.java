package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.agarwal on 25/5/17.
 */

public class AddToBagRequest {
    @SuppressWarnings("unused")
    private static final String TAG = "AddToBagRequest";

    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_ADD_QTY = "add_qty";
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_PRODUCT_ID = "productId";

    private String mProductId;
    private int mQty = 1;

    public AddToBagRequest(String productId, int qty) {
        mProductId = productId;
        mQty = qty;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_PRODUCT_ID, mProductId);
            jsonObject.put(KEY_ADD_QTY, mQty);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
