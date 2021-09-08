package com.webkul.mobikul.marketplace.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 5/3/18 in Mobikul_Odoo_Application.
 */

public class SellerReviewRequest {
    private static final String KEY_GRADE = "rating";
    private static final String KEY_TITLE = "title";
    private static final String KEY_MAG = "msg";
    private float grade;
    private String title;
    private String msg;

    public SellerReviewRequest(float grade, String title, String msg) {
        this.grade = grade;
        this.title = title;
        this.msg = msg;
    }

    public float getGrade() {
        return grade;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        }
        return title;
    }

    public String getMsg() {
        if (msg == null) {
            return "";
        }
        return msg;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject();
            jsonObject.put(KEY_GRADE, getGrade());
            jsonObject.put(KEY_TITLE, getTitle());
            jsonObject.put(KEY_MAG, getMsg());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
