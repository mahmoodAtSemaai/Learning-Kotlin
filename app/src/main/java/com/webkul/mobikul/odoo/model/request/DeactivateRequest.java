package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

public class DeactivateRequest {

    @SuppressWarnings("unused")
    private static final String TAG = "DeactivateRequest";
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_TYPE = "type";

    private String type;

    public DeactivateRequest(String dtype) {
        type = dtype;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
