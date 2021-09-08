package com.webkul.mobikul.odoo.model.customer.signin;

/**
 * @author shubham.agarwal
 */

public class SignInForgetPasswordData {

    @SuppressWarnings("unused")
    private static final String TAG = "SignInForgetPasswordDat";

    private String mUsername;

    public SignInForgetPasswordData(String username) {
        this.mUsername = username;
    }


    public String getUsername() {
        if (mUsername == null) {
            return "";
        }
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }
}
