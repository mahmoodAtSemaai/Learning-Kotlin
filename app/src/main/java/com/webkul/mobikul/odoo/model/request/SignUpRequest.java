package com.webkul.mobikul.odoo.model.request;

import android.content.Context;
import androidx.annotation.NonNull;

import com.webkul.mobikul.odoo.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.agarwal on 13/6/17.
 */

public class SignUpRequest extends RegisterDeviceTokenRequest {
    public static final String OAUTH_PROVIDER_GOOGLE = "GMAIL";
    public static final String OAUTH_PROVIDER_FACEBOOK = "FACEBOOK";
    public static final String OAUTH_PROVIDER_TWITTER = "TWITTER";

    @SuppressWarnings("unused")
    private static final String TAG = "SignUpRequest";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SOCIAL_LOGIN = "isSocialLogin";
    private static final String KEY_AUTH_PROVIDER = "authProvider";
    private static final String KEY_AUTH_USER_ID = "authUserId";
    private static final String KEY_AUTH_TOKEN = "authToken";
    private static final String KEY_IS_SELLER = "is_seller";
    private static final String KEY_URL_HANDLER = "url_handler";
    private static final String KEY_ID_COUNTRY = "country_id";
    private final String mName;
    private final String mLogin;
    private final String mPassword;
    private String mAuthAccessToken;
    private boolean mIsSocialLogin;
    private boolean isSeller = false;
    private String profileURL;
    private String idCountry;
    private String mAuthProvider;
    private String mAuthUserId;

    public SignUpRequest(Context context, String name, String login, @NonNull String password, boolean isSocialLogin, boolean isSeller, String profileURL, String countryID) {
        super(context);
        mName = name;
        mLogin = login;
        mPassword = password;
        mIsSocialLogin = isSocialLogin;
        this.isSeller = isSeller;
        this.profileURL = profileURL;
        this.idCountry = countryID;
    }

   /* public SignUpRequest toSignUpRequest(){

    }*/

    public SignUpRequest(Context context, String name, String login, @NonNull String password, boolean isSocialLogin, String authProvider, String authUserId) {
        this(context, name, login, password, isSocialLogin, false, "", "");
        mAuthProvider = authProvider;
        mAuthUserId = authUserId;
    }

    public SignUpRequest(Context context, String name, String login, @NonNull String password, boolean isSocialLogin, String authProvider, String authUserId, String authAccessToken) {
        this(context, name, login, password, isSocialLogin, authProvider, authUserId);
        mAuthAccessToken = authAccessToken;
    }


    public String getName() {
        if (mName == null) {
            return "";
        }
        return mName;
    }

    public String getLogin() {
        if (mLogin == null) {
            return "";
        }
        return mLogin;
    }

    public String getPassword() {
        //noinspection ConstantConditions
        if (mPassword == null) {
            return "";
        }
        if (isSocialLogin()) {
            return getAuthUserId();
        }

        return mPassword;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isSocialLogin() {
        return mIsSocialLogin;
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
    public String getAuthAccessToken() {
        if (mAuthAccessToken == null) {
            return "";
        }
        return mAuthAccessToken;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_NAME, getName());
            if (isSocialLogin() && getAuthProvider().equals(OAUTH_PROVIDER_FACEBOOK)) {
//                jsonObject.put(KEY_EMAIL, getLogin());
                jsonObject.put(KEY_LOGIN, getLogin());
            } else {
                jsonObject.put(KEY_LOGIN, getLogin());
            }
            jsonObject.put(KEY_PASSWORD, getPassword());
            jsonObject.put(KEY_SOCIAL_LOGIN, isSocialLogin());
            if (BuildConfig.isMarketplace && isSeller) {
                jsonObject.put(KEY_IS_SELLER, isSeller);
                jsonObject.put(KEY_URL_HANDLER, profileURL);
                jsonObject.put(KEY_ID_COUNTRY, idCountry);
            }
            if (isSocialLogin()) {
                jsonObject.put(KEY_AUTH_PROVIDER, getAuthProvider());
                jsonObject.put(KEY_AUTH_USER_ID, getAuthUserId());
                if (getAuthProvider().equals(OAUTH_PROVIDER_FACEBOOK) || getAuthProvider().equals(OAUTH_PROVIDER_TWITTER)) {
                    jsonObject.put(KEY_AUTH_TOKEN, getAuthAccessToken());
                }
            }

            updateRequestJson(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


}
