package com.webkul.mobikul.odoo.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.handler.fingerprint.FingerprintHandler;
import com.webkul.mobikul.odoo.model.customer.signin.LoginRequestData;
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse;
import com.webkul.mobikul.odoo.model.customer.signup.SignUpData;
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

public class FingerPrintLoginHelper {

    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "AndroidKeyStore";
    //For fingerprint
    private KeyStore keyStore;
    private Cipher cipher;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askForFingerprintLogin(Context context, LoginResponse loginResponse, LoginRequestData lData, SignUpResponse signUpResponse, SignUpData sData) {

        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);


        if (fingerprintManager != null && fingerprintManager.isHardwareDetected()) {

            //Ask to store fingerprint.
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context);
            sweetAlertDialog.setTitleText(context.getString(R.string.fingerprint_login_header));
            sweetAlertDialog.setContentText(context.getString(R.string.ask_for_fingerprint_login));
            sweetAlertDialog.setConfirmText(context.getString(R.string.ok));
            sweetAlertDialog.setCancelText(context.getString(R.string.cancel));
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    sweetAlertDialog.dismissWithAnimation();

                    if (checkFingerprintLoginPermissions(context)) {
                        generateKey();

                        SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(context);
                        sweetAlertDialog1.setContentText(context.getString(R.string.place_your_finger_on_sensor));
                        sweetAlertDialog1.setTitleText(context.getString(R.string.fingerprint_authentication));
                        sweetAlertDialog1.setConfirmText(context.getString(R.string.cancel));
                        sweetAlertDialog1.showCancelButton(false);

                        FingerprintHandler helper = new FingerprintHandler(fingerprintManager, context, sweetAlertDialog1, null);

                        sweetAlertDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (!AppSharedPref.getIsAllowedFingerprintLogin(context)) {
                                    helper.stopListening();
                                } else {
                                    AppSharedPref.setCustomerLoginBase64StrForFingerprint(context, AppSharedPref.getCustomerLoginBase64Str(context));
                                }

                                goToHomePage(context, loginResponse, lData, signUpResponse, sData);
                            }
                        });
                        sweetAlertDialog1.setCancelable(false);

                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject;
                            cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            helper.startAuth(fingerprintManager, cryptoObject);

                            sweetAlertDialog1.show();
                        } else {
                            StyleableToast.makeText(context, context.getString(R.string.fingerprint_error), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                            AppSharedPref.setIsAllowedFingerprintLogin(context, false);
                            goToHomePage(context, loginResponse, lData, signUpResponse, sData);
                        }
                    } else {
                        AppSharedPref.setIsAllowedFingerprintLogin(context, false);
                        goToHomePage(context, loginResponse, lData, signUpResponse, sData);
                    }

                }
            });
            sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                    AppSharedPref.setIsAllowedFingerprintLogin(context, false);
                    goToHomePage(context, loginResponse, lData, signUpResponse, sData);
                }
            });
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();

            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fp_40px));

            LinearLayout linearLayout = sweetAlertDialog.findViewById(R.id.loading);
            int index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));

            linearLayout.addView(imageView, index - 1);
        } else {
            AppSharedPref.setIsAllowedFingerprintLogin(context, false);
            goToHomePage(context, loginResponse, lData, signUpResponse, sData);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkFingerprintLoginPermissions(Context context) {

        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            StyleableToast.makeText(context, context.getString(R.string.no_fingerprint_permission), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
        } else {
            // Check whether at least one fingerprint is registered
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (fingerprintManager != null && keyguardManager != null) {
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        StyleableToast.makeText(context, context.getString(R.string.no_fingerprint_registered), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                        return false;
                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            StyleableToast.makeText(context, context.getString(R.string.no_lock_screen_security), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                            return false;
                        } else {
                            Log.v("keyguardCheck", keyguardManager.isKeyguardSecure() + " , " + keyguardManager.isDeviceSecure());

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void goToHomePage(Context mContext, LoginResponse loginResponse, LoginRequestData lData, SignUpResponse signUpResponse, SignUpData sData) {
        if (loginResponse != null && lData != null) {
            FirebaseAnalyticsImpl.logLoginEvent(mContext, loginResponse.getCustomerId(), AppSharedPref.getCustomerName(mContext));
            loginResponse.updateSharedPref(mContext, lData.getPassword());
            StyleableToast.makeText(mContext, mContext.getString(R.string.login_successful_message), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
            ApiRequestHelper.callHomePageApi(mContext);
        } else if (signUpResponse != null) {
            FirebaseAnalyticsImpl.logSignUpEvent(mContext, signUpResponse.getCustomerId(), signUpResponse.getLogin().getCustomerName());
            signUpResponse.getLogin().updateSharedPref(mContext, sData.getPassword());
            IntentHelper.continueShopping(mContext, signUpResponse.getHomePageResponse());
            ((Activity) mContext).finish();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
        }


        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
        }


        try {
            keyStore.load(null);
            if (keyGenerator != null) {
                keyGenerator.init(new
                        KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(
                                KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());

                keyGenerator.generateKey();
            }
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            //throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            return false;
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            return false;
        }
    }


}
