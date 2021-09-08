package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 24/7/17.
 */

public class ProductReviewRequest {
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_TEMPLATE_ID = "template_id";

    private int mTemplateId;

    public ProductReviewRequest(String templateId) {
        mTemplateId = Integer.parseInt(templateId);
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_TEMPLATE_ID, mTemplateId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
