package com.webkul.mobikul.marketplace.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 9/4/18 in Mobikul_Odoo_Application.
 */

public class AskToAdminRequest {
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "body";
    private String title;
    private String description;

    public AskToAdminRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        }
        return title;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_TITLE, getTitle());
            jsonObject.put(KEY_DESCRIPTION, getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
