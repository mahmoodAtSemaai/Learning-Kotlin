package com.webkul.mobikul.marketplace.odoo.model.request;

import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 31/10/17 in Mobikul_Odoo_Application.
 */

public class SellerCollectionRequest extends BaseLazyRequest {
    @SuppressWarnings("unused")
    private static final String TAG = "SellerCollectionRequest";
    private static final String KEY_MARKETPLACE_SELLER_ID = "marketplace_seller_id";
    private static final String KEY_DOMAIN = "domain";
    private final String mSellerId;

    public SellerCollectionRequest(String sellerId, int offset, int limit) {
        super(offset, limit);
        mSellerId = sellerId;

    }

    private String getmSellerId() {
        if (mSellerId == null) {
            return "";
        }
        return mSellerId;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(super.toString());
//            JSONObject object = new JSONObject();
//            object.put(KEY_MARKETPLACE_SELLER_ID, getmSellerId());
//            JSONArray array = new JSONArray();
//            array.put(object);
            jsonObject.put(KEY_DOMAIN, "[('marketplace_seller_id','='," + getmSellerId() + ")]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
