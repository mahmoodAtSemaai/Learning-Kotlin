package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * REQUEST DATA THAT MUST BE SEND TO EVERY PAGE...
 * <p>
 * Created by shubham.agarwal on 2/5/17.
 */

public class AuthenticationRequest {
    @SuppressWarnings("unused")
    private static final String TAG = "AuthenticationRequest";

    private static final String KEY_USER = "login";
    private static final String KEY_PASSWORD = "pwd";


    private static final String KEY_AUTH_PROVIDER = "authProvider";
    private static final String KEY_AUTH_USER_ID = "authUserId";

    private String usr;
    private String pwd;
    private String mAuthProvider;
    private String mAuthUserId;
    private boolean mIsSocialLogin;

    public AuthenticationRequest(String usr, String pwd) {
        this.usr = usr;
        this.pwd = pwd;
    }

    public AuthenticationRequest(String authProvider, String authUserId, boolean isSocialLogin) {
        mAuthProvider = authProvider;
        mAuthUserId = authUserId;
        mIsSocialLogin = isSocialLogin;
    }


    @SuppressWarnings("WeakerAccess")
    public String getUsr() {
        if (usr == null) {
            return "";
        }
        return usr;
    }


    @SuppressWarnings("WeakerAccess")
    public String getPwd() {
        if (pwd == null) {
            return "";
        }
        return pwd;
    }

    @SuppressWarnings("WeakerAccess")
    public String getAuthProvider() {
        if (mAuthProvider == null) {
            return "";
        }
        return mAuthProvider;
    }

    @SuppressWarnings("WeakerAccess")
    public String getAuthUserId() {
        if (mAuthUserId == null) {
            return "";
        }
        return mAuthUserId;
    }


    @SuppressWarnings("WeakerAccess")
    public boolean isSocialLogin() {
        return mIsSocialLogin;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (isSocialLogin()) {
                jsonObject.put(KEY_AUTH_PROVIDER, getAuthProvider());
                jsonObject.put(KEY_AUTH_USER_ID, getAuthUserId());
            } else {
                jsonObject.put(KEY_USER, getUsr());
                jsonObject.put(KEY_PASSWORD, getPwd());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String getForgetPasswordRequestStr() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_USER, getUsr());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
