package com.webkul.mobikul.odoo.activity;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.ActivitySignInSignUpBinding;
import com.webkul.mobikul.odoo.dialog_frag.FingerprintAuthenticationDialogFragment;
import com.webkul.mobikul.odoo.fragment.LoginFragment;
import com.webkul.mobikul.odoo.fragment.SignUpFragment;
import com.webkul.mobikul.odoo.handler.generic.SignInSignUpActivityHandler;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.ApiRequestHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.BlurBuilder;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse;
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest;
import com.webkul.mobikul.odoo.model.request.SignUpRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.model.request.SignUpRequest.OAUTH_PROVIDER_GOOGLE;
import static com.webkul.mobikul.odoo.model.request.SignUpRequest.OAUTH_PROVIDER_TWITTER;

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
    public ActivitySignInSignUpBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TAG);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in_sign_up);

        /*blur image*/

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.login_bg);
//            Bitmap blurredBitmap = BlurBuilder.blur(this, originalBitmap);
//            mBinding.getRoot().setBackground(new BitmapDrawable(getResources(), blurredBitmap));
//        }

        signInSignUpActivityHandler = new SignInSignUpActivityHandler(this);
        mBinding.setHandler(signInSignUpActivityHandler);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra("REQ_FOR")) {
            if (getIntent().getExtras().getString("REQ_FOR", "").equals("Open new shop")) {
                signInSignUpActivityHandler.signUp();
            }
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
