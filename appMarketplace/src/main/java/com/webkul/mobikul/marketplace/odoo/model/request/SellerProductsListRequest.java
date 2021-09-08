package com.webkul.mobikul.marketplace.odoo.model.request;

import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 22/2/18 in Mobikul_Odoo_Application.
 */

public class SellerProductsListRequest extends BaseLazyRequest {
    @SuppressWarnings("unused")
    private static final String TAG = "SellerProductsListRequest";
    private static final String KEY_STATE = "state";
    private static final String KEY_OPERATOR = "operator";
    private final String mState;

    public SellerProductsListRequest(String state, int offset, int limit) {
        super(offset, limit);
        mState = state;

    }

    private String getmState() {
        if (mState == null) {
            return "";
        }
        return mState;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(super.toString());
            jsonObject.put(KEY_STATE, getmState());
            jsonObject.put(KEY_OPERATOR, "=");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
