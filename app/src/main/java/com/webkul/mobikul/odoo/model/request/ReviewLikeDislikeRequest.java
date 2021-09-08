package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 28/7/17.
 */

public class ReviewLikeDislikeRequest {

    @SuppressWarnings("FieldCanBeLocal")
    private final String REVIEW_ID = "review_id";
    @SuppressWarnings("FieldCanBeLocal")
    private final String IS_HELPFUL = "ishelpful";

    public String getmReviewId() {
        return String.valueOf(mReviewId);
    }

    private int mReviewId;
    private boolean mIsHelpful;

    public ReviewLikeDislikeRequest(int reviewID, boolean isHelpful) {
        mReviewId = reviewID;
        mIsHelpful = isHelpful;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(REVIEW_ID, mReviewId);
            jsonObject.put(IS_HELPFUL, mIsHelpful);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
