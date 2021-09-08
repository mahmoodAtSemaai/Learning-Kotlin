package com.webkul.mobikul.odoo.model.request;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import com.google.firebase.iid.FirebaseInstanceId;
import com.webkul.mobikul.odoo.helper.AppSharedPref;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.agarwal on 13/6/17.
 */

public class RegisterDeviceTokenRequest {
    @SuppressWarnings("unused")
    private static final String TAG = "RegisterDeviceTokenRequ";

    @SuppressWarnings("FieldCanBeLocal")
    protected final String KEY_TOKEN = "fcmToken";
    @SuppressWarnings("FieldCanBeLocal")
    protected final String KEY_CUSTOMER_ID = "customerId";
    @SuppressWarnings("FieldCanBeLocal")
    protected final String KEY_DEVICE_ID = "fcmDeviceId";
    protected Context mContext;

    public RegisterDeviceTokenRequest(Context context) {
        mContext = context;
    }

    @SuppressLint("HardwareIds")
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        updateRequestJson(jsonObject);
        return jsonObject.toString();
    }

    protected void updateRequestJson(JSONObject jsonObject) {
        try {
            jsonObject.put(KEY_TOKEN, FirebaseInstanceId.getInstance().getToken() == null ? "" : FirebaseInstanceId.getInstance().getToken());
            jsonObject.put(KEY_CUSTOMER_ID, AppSharedPref.getCustomerId(mContext));
            jsonObject.put(KEY_DEVICE_ID, Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
