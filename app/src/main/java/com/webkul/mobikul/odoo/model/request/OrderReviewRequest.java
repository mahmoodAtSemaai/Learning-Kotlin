package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.agarwal on 26/5/17.
 */

public class OrderReviewRequest {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderReviewRequest";

    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_SHIPPING_ADDRESS_ID = "shippingAddressId";
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_ACQUIRER_ID = "acquirerId";
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_SHIPPING_ID = "shippingId";
    private final String mShippingAddressId;
    private final String mAcquirerId;
    private String shippingMethodId;


    public OrderReviewRequest(String shippingAddressId, String shippingMethodId, String acquirerId) {
        mShippingAddressId = shippingAddressId;
        this.shippingMethodId = shippingMethodId;
        mAcquirerId = acquirerId;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_SHIPPING_ADDRESS_ID, mShippingAddressId);
            jsonObject.put(KEY_ACQUIRER_ID, mAcquirerId);
            if (!shippingMethodId.isEmpty()) {
                jsonObject.put(KEY_SHIPPING_ID, shippingMethodId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
