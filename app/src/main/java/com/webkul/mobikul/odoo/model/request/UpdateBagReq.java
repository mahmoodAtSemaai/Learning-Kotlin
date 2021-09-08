package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.agarwal on 24/5/17.
 */

public class UpdateBagReq {
    @SuppressWarnings("unused")
    private static final String TAG = "UpdateBagReq";
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_SET_QTY = "set_qty";

    private int mQty = 1;

    public UpdateBagReq(int qty) {
        mQty = qty;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_SET_QTY, mQty);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
