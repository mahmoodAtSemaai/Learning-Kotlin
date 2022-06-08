package com.webkul.mobikul.odoo.activity;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.constant.BundleConstant;
import com.webkul.mobikul.odoo.databinding.ActivitySignInSignUpBinding;
import com.webkul.mobikul.odoo.dialog_frag.FingerprintAuthenticationDialogFragment;
import com.webkul.mobikul.odoo.fragment.LoginFragment;
import com.webkul.mobikul.odoo.fragment.SignUpFragment;
import com.webkul.mobikul.odoo.handler.generic.SignInSignUpActivityHandler;
import com.webkul.mobikul.odoo.helper.ApiRequestHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.IntentHelper;

/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

public class SignInSignUpActivity extends BaseActivity {

    private SignInSignUpActivityHandler signInSignUpActivityHandler;
    @SuppressWarnings("unused")
    private static final String TAG = "SignInSignUpActivity";
    public ActivitySignInSignUpBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setTitle(TAG);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in_sign_up);
        binding.tvContactWhatsappAdmin.setOnClickListener(view -> {
            IntentHelper.goToWhatsApp(this);
        });

        signInSignUpActivityHandler = new SignInSignUpActivityHandler(this);
        binding.setHandler(signInSignUpActivityHandler);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra("REQ_FOR")) {
            if (getIntent().getExtras().getString("REQ_FOR", "").equals("Open new shop")) {
                signInSignUpActivityHandler.signUp();
            }
        }

    }

    private void setLanguage() {
        String languageCode = AppSharedPref.getLanguageCode(this);
        if (languageCode.isEmpty()) {
            AppSharedPref.setLanguageCode(this, getString(R.string.ind_lang));
            setLocale(this, false);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras() != null &&
                intent.getExtras().containsKey(BundleConstant.BUNDLE_KEY_SIGNUP_SCREEN)) {
            signInSignUpActivityHandler.signUp();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Fragment fragment = mSupportFragmentManager.findFragmentByTag(SignUpFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            mSupportFragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        Helper.hideKeyboard(SignInSignUpActivity.this);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra("REQ_FOR")) {
            if (getIntent().getExtras().getString("REQ_FOR", "").equals("Open new shop")) {
                super.onBackPressed();
                return;
            }
        }
        Fragment fragment;
        fragment = mSupportFragmentManager.findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            mSupportFragmentManager.beginTransaction().remove(fragment).commit();
            return;
        }

        fragment = mSupportFragmentManager.findFragmentByTag(SignUpFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            mSupportFragmentManager.beginTransaction().remove(fragment).commit();
            return;
        }

        fragment = mSupportFragmentManager.findFragmentByTag(FingerprintAuthenticationDialogFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            mSupportFragmentManager.beginTransaction().remove(fragment).commit();
            return;
        }


        if (!AppSharedPref.isAllowedGuestCheckout(this)) {
            finish();
            return;
        }

        /*in case application is direclty called first time from Splash Screen .*/
        //noinspection ConstantConditions
        if (getIntent().getExtras().getString(BUNDLE_KEY_CALLING_ACTIVITY).equals(SplashScreenActivity.class.getSimpleName())) {
            ApiRequestHelper.callHomePageApi(this);
        } else {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public String getScreenTitle() {
        return TAG;
    }

}
