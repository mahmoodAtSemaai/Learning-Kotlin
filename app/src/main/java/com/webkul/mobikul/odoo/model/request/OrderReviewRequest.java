package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_USE_POINTS = "use_points";
    private final String KEY_LINE_IDS = "lineIds";
    private final String shippingAddressId;
    private final String paymentAcquirerId;
    private String shippingMethodId;
    private Boolean usePoints;
    private ArrayList<Integer> lineIds;


    public OrderReviewRequest(String shippingAddressId, String shippingMethodId, String acquirerId, Boolean usePoints, ArrayList<Integer> lineIds) {
        this.shippingAddressId = shippingAddressId;
        this.shippingMethodId = shippingMethodId;
        this.paymentAcquirerId = acquirerId;
        this.usePoints = usePoints;
        this.lineIds = lineIds;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_SHIPPING_ADDRESS_ID, shippingAddressId);
            jsonObject.put(KEY_ACQUIRER_ID, paymentAcquirerId);
            if (!shippingMethodId.isEmpty()) {
                jsonObject.put(KEY_SHIPPING_ID, shippingMethodId);
            }
            jsonObject.put(KEY_USE_POINTS, usePoints);
            jsonObject.put(KEY_LINE_IDS, lineIds.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
