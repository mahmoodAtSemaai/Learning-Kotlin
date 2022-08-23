package com.webkul.mobikul.odoo.handler.generic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;

import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.features.authentication.presentation.AuthenticationActivity;
import com.webkul.mobikul.odoo.constant.BundleConstant;
import com.webkul.mobikul.odoo.dialog_frag.FingerprintAuthenticationDialogFragment;
import com.webkul.mobikul.odoo.fragment.SignUpFragment;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FingerPrintLoginHelper;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.ui.signUpAuth.SignUpAuthActivity;

import java.security.KeyStore;

import javax.crypto.Cipher;


/**
 * Created by shubham.agarwal on 12/6/17.
 */

public class SignInSignUpActivityHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "SignInSignUpActivityHan";
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "AndroidKeyStore";
    private Context context;
    //For fingerprint
    private KeyStore keyStore;
    private Cipher cipher;


    public SignInSignUpActivityHandler(Context context) {
        this.context = context;
    }

    public void closeActivity() {
        ((Activity) context).onBackPressed();
    }

    public void login() {
        context.startActivity(new Intent(context, AuthenticationActivity.class));
    }

    public void signUp() {
        context.startActivity(new Intent(context, SignUpAuthActivity.class));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void fingerprintLogin() {
        if (AppSharedPref.getIsAllowedFingerprintLogin(context)) {

            FingerPrintLoginHelper fingerPrintLoginHelper = new FingerPrintLoginHelper();
            if (fingerPrintLoginHelper.checkFingerprintLoginPermissions(context)) {
                fingerPrintLoginHelper.generateKey();
                if (fingerPrintLoginHelper.cipherInit()) {
                    FingerprintAuthenticationDialogFragment fragment = new FingerprintAuthenticationDialogFragment();

                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    fragment.setCryptoObject(cryptoObject);
                    FragmentHelper.addFragment(android.R.id.content, context, fragment, FingerprintAuthenticationDialogFragment.class.getSimpleName(), false, false);
                    fragment.setCancelable(true);
                } else {
                    StyleableToast.makeText(context, context.getString(R.string.fingerprint_error), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                }
            }


        } else {
            StyleableToast.makeText(context, "Please login with e-mail once to enable fingerprint", Toast.LENGTH_LONG, R.style.GenericStyleableToast).show();
        }
    }

}