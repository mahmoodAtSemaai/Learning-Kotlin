package com.webkul.mobikul.odoo.model.request;

import android.content.Context;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 25/7/17.
 */

public class AddProductReviewRequest {

    @SuppressWarnings("unused")
    private static final String KEY_RATING = "rate";
    private static final String KEY_TITLE = "title";
    private static final String KEY_REVIEW = "detail";
    private static final String KEY_TEMPLATE_ID = "template_id";
    private final int mRating;
    private final String mTitle;
    private final String mReview;
    private String mTemplateId;

    public AddProductReviewRequest(Context context, @NonNull int rate, @NonNull String title, @NonNull String review, @NonNull String templateId) {
        mRating = rate;
        mTitle = title;
        mReview = review;
        mTemplateId = templateId;
    }

    public int getmRating() {
        return mRating;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmReview() {
        return mReview;
    }

    public String getmTemplateId() {
        return mTemplateId;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_RATING, getmRating());
            jsonObject.put(KEY_TITLE, getmTitle());
            jsonObject.put(KEY_REVIEW, getmReview());
            jsonObject.put(KEY_TEMPLATE_ID, Integer.parseInt(getmTemplateId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
