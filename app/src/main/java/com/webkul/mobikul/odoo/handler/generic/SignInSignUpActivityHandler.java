package com.webkul.mobikul.odoo.handler.generic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.gson.Gson;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.dialog_frag.FingerprintAuthenticationDialogFragment;
import com.webkul.mobikul.odoo.fragment.LoginFragment;
import com.webkul.mobikul.odoo.fragment.SignUpFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FingerPrintLoginHelper;
import com.webkul.mobikul.odoo.helper.FragmentHelper;

import java.security.KeyStore;
import java.util.Arrays;

import javax.crypto.Cipher;


/**
 * Created by shubham.agarwal on 12/6/17.
 */

public class SignInSignUpActivityHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "SignInSignUpActivityHan";
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "AndroidKeyStore";
    private Context mContext;
    //For fingerprint
    private KeyStore keyStore;
    private Cipher cipher;


    public SignInSignUpActivityHandler(Context context) {
        mContext = context;
    }

    public void closeActivity() {
        ((Activity) mContext).onBackPressed();
    }

    public void login() {
        FragmentHelper.addFragment(android.R.id.content, mContext, LoginFragment.newInstance(), LoginFragment.class.getSimpleName(), false, false);
    }

    public void signUp() {
        FragmentHelper.addFragment(android.R.id.content, mContext, SignUpFragment.newInstance(), SignUpFragment.class.getSimpleName(), false, false);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void fingerprintLogin() {
        if (AppSharedPref.getIsAllowedFingerprintLogin(mContext)) {

            FingerPrintLoginHelper fingerPrintLoginHelper = new FingerPrintLoginHelper();
            if (fingerPrintLoginHelper.checkFingerprintLoginPermissions(mContext)) {
                fingerPrintLoginHelper.generateKey();
                if (fingerPrintLoginHelper.cipherInit()) {
                    FingerprintAuthenticationDialogFragment fragment = new FingerprintAuthenticationDialogFragment();

                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    fragment.setCryptoObject(cryptoObject);
                    FragmentHelper.addFragment(android.R.id.content, mContext, fragment, FingerprintAuthenticationDialogFragment.class.getSimpleName(), false, false);
                    fragment.setCancelable(true);
                } else {
                    StyleableToast.makeText(mContext, mContext.getString(R.string.fingerprint_error), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                }
            }


        } else {
            StyleableToast.makeText(mContext, "Please login with e-mail once to enable fingerprint", Toast.LENGTH_LONG, R.style.GenericStyleableToast).show();
        }
    }

}