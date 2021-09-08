package com.webkul.mobikul.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.agarwal on 29/5/17.
 */

public class SaveCustomerDetailRequest {

    @SuppressWarnings("unused")
    private static final String TAG = "SaveCustomerDetailRequest";

    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_NAME = "name";
    private final String KEY_PASSWORD = "password";
    private final String KEY_ENCODED_IMAGE = "image";
    private final String KEY_ENCODED_IMAGE_BANNER = "bannerImage";
    private final String mName;
    private final String mPassword;
    private String mEncodedImage;
    private String mEncodedImageBanner;


    public SaveCustomerDetailRequest(String name, String password, String encodedImage, String encodedImageBanner) {
        mName = name;
        mPassword = password;
        mEncodedImage = encodedImage;
        mEncodedImageBanner = encodedImageBanner;
    }

    @SuppressWarnings("WeakerAccess")
    public String getName() {
        if (mName == null) {
            return "";
        }
        return mName;
    }

    @SuppressWarnings("WeakerAccess")
    public String getPassword() {
        if (mPassword == null) {
            return "";
        }

        return mPassword;
    }

    @SuppressWarnings("WeakerAccess")
    public String getEncodedImage() {
        if (mEncodedImage == null) {
            return "";
        }
        return mEncodedImage;
    }

    @SuppressWarnings("WeakerAccess")
    public String getmEncodedImageBanner() {

        if (mEncodedImageBanner == null) {
            return "";
        }

        return mEncodedImageBanner;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {

            if (!getName().isEmpty()) {
                jsonObject.put(KEY_NAME, getName());
            }

            if (!getPassword().isEmpty()) {
                jsonObject.put(KEY_PASSWORD, getPassword());
            }

            if (!getEncodedImage().isEmpty()) {
                jsonObject.put(KEY_ENCODED_IMAGE, getEncodedImage());
            }
            if (!getmEncodedImageBanner().isEmpty()) {
                jsonObject.put(KEY_ENCODED_IMAGE_BANNER, getmEncodedImageBanner());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
