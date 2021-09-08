package com.webkul.mobikul.odoo.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.database.SaveData;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.ActivitySplashScreenBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.NetworkHelper;
import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.model.extra.SplashScreenActivityData;
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.BuildConfig.APP_PLAYSTORE_URL;
import static com.webkul.mobikul.odoo.BuildConfig.isMarketplace;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_CATEGORY;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_CUSTOM;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_NONE;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_PRODUCT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_NOTIFICATION_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_SEARCH_DOMAIN;
import static com.webkul.mobikul.odoo.helper.AppSharedPref.CUSTOMER_PREF;
import static com.webkul.mobikul.odoo.helper.AppSharedPref.getLanguageCode;
import static com.webkul.mobikul.odoo.helper.CatalogHelper.CatalogProductRequestType.SEARCH_DOMAIN;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class SplashScreenActivity extends BaseActivity  {
    @SuppressWarnings("unused")
    private static final String TAG = "SplashScreenActivity";
    private static final int RC_UPDATE_APP_FROM_PLAYSTORE = 1;
    private ActivitySplashScreenBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, ?> allEntries = AppSharedPref.getSharedPreference(this, CUSTOMER_PREF).getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }

        if (!AppSharedPref.getLanguageCode(this).isEmpty()) {
            BaseActivity.setLocale(this, false);
        }

        int darkModeFlag = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDark = AppSharedPref.isDarkMode(this);
        if (isDark || darkModeFlag == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            AppSharedPref.setDarkMode(this, true);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        Helper.logIntentBundleData(getIntent());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        mBinding.setData(new SplashScreenActivityData());    // starting progressBar
        FirebaseAnalyticsImpl.logAppOpenEvent(this);
        callApi();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_UPDATE_APP_FROM_PLAYSTORE) {
            callApi();
        }
    }


    private void callApi() {
        Log.d("TAG", "SplashScreenActivity callApi: ----------------------------");


        /*PERFORM ACTION ON NOTIFICATION */
        if ((AppSharedPref.isLoggedIn(SplashScreenActivity.this) || AppSharedPref.isAllowedGuestCheckout(this)) && getIntent().getExtras() != null && getIntent().hasExtra(BUNDLE_KEY_NOTIFICATION_ID)) {
            Intent notificationIntent = getNotificationIntent();
            if (notificationIntent != null) {
                notificationIntent.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SplashScreenActivity.class.getSimpleName());
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getSplashPageData(notificationIntent);
                return;
            }
        }


        if (AppSharedPref.isLoggedIn(SplashScreenActivity.this) || AppSharedPref.isAllowedGuestCheckout(this)) {
            Log.d("TAG", "SplashScreenActivity callApi isNetworkAvailable :-->" +NetworkHelper.isNetworkAvailable(SplashScreenActivity.this));
            if (NetworkHelper.isNetworkAvailable(this)) {
                HitApiForHomePage();
            } else {
                SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(SplashScreenActivity.this);
                HomePageResponse homePageResponse = sqlLiteDbHelper.getHomeScreenData();
                if (homePageResponse != null) {
                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
                    startActivity(intent);
                } else {
                    HitApiForHomePage();
                }
            }
            /*adding subscribe on here instead of zip to create the observable on io thread. */

        } else {
            Log.d("TAG", "SplashScreenActivity callApi :--> else waale part mein gaaya h ");
            Intent i = new Intent(SplashScreenActivity.this, SignInSignUpActivity.class);
            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SplashScreenActivity.class.getSimpleName());
            getSplashPageData(i);
        }
    }

    public void callApi_() {
        /*PERFORM ACTION ON NOTIFICATION */
        if ((AppSharedPref.isLoggedIn(SplashScreenActivity.this) || AppSharedPref.isAllowedGuestCheckout(this)) && getIntent().getExtras() != null && getIntent().hasExtra(BUNDLE_KEY_NOTIFICATION_ID)) {
            Intent notificationIntent = getNotificationIntent();
            if (notificationIntent != null) {
                notificationIntent.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SplashScreenActivity.class.getSimpleName());
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getSplashPageData(notificationIntent);
                return;
            }
        }

        if (AppSharedPref.isLoggedIn(SplashScreenActivity.this) || AppSharedPref.isAllowedGuestCheckout(this)) {
            Log.d("TAG", "SplashScreenActivity callApi isNetworkAvailable :-->" +NetworkHelper.isNetworkAvailable(SplashScreenActivity.this));
            if (AppSharedPref.getLanguageCode(this).equals("") || AppSharedPref.isAppRunFirstTime(this)) {
                HomePageResponse homePageResponse = getHomeLocalFileData();
                SplashScreenResponse splashScreenResponse = getSplashLocalFileData();
                if (homePageResponse != null) {
                    AppSharedPref.setIsAppRunFirstTime(this, false);
                    Intent intentHomeActivity = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    intentHomeActivity.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
                    if (splashScreenResponse != null) {
                        splashScreenResponse.updateSharedPref(SplashScreenActivity.this);
                        new SaveData(SplashScreenActivity.this, splashScreenResponse);
                        startActivity(intentHomeActivity);
                    } else
                        getSplashPageData(intentHomeActivity);
                } else {
                    // because this is first time, app is running. So there is no data in DB, that's why call home api.
                    HitApiForHomePage();
                }
            } else {
                if (AppSharedPref.isLanguageChange(this)) {
                    // after changing language, get fresh home & splash data
                    HitApiForHomePage();
                } else {
                    SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(SplashScreenActivity.this);
                    HomePageResponse homePageResponse = sqlLiteDbHelper.getHomeScreenData();
                    SplashScreenResponse splashScreenResponse = sqlLiteDbHelper.getSplashScreenData();
                    if (homePageResponse != null && splashScreenResponse != null) {
                        splashScreenResponse.updateSharedPref(SplashScreenActivity.this);
                        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                        intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
                        startActivity(intent);
                    } else {
                        HitApiForHomePage();
                    }
                }
            }
        } else {
            // first time call SignInSignUpActivity.
            // If local splash data exists, then call SignInSignUpActivity with splashResponseData
            // Else pass control to getSplashPageData() method which will call splash Api.
            Intent i = new Intent(SplashScreenActivity.this, SignInSignUpActivity.class);
            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SplashScreenActivity.class.getSimpleName());
            SplashScreenResponse splashScreenResponse = getSplashLocalFileData();
            if (splashScreenResponse != null) {
                splashScreenResponse.updateSharedPref(SplashScreenActivity.this);
                new SaveData(SplashScreenActivity.this, splashScreenResponse);
                startActivity(i);
            } else
                getSplashPageData(i);
        }
    }

    /**
     * Method is used prepare SplashScreenResponse object from saved .json file and return it to calling function.
     * If app is confugured with marketplace, then fetch data from  'mp_splash_page_data' file else 'splash_page_data' file.
     * If there is any exception occur due to any reason, it will return null to calling function.
     * @date 23-Aug-2019 3:28 PM
     * @update 23-Aug-2019 3:28 PM
     * @author Ayushi Agarwal
     * @return either SplashScreenResponse or null
     */
    private SplashScreenResponse getSplashLocalFileData() {
        try {
            InputStream homePageFileData;
            if (isMarketplace) {
                homePageFileData = getAssets().open("mp_splash_page_data.json");
            } else
                homePageFileData = getAssets().open("splash_page_data.json");
            BufferedReader r = new BufferedReader(new InputStreamReader(homePageFileData));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }
            Gson mGson = new Gson();
            return mGson.fromJson(total.toString(), SplashScreenResponse.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Method is used prepare HomePageResponse object from saved .json file and return it to calling function.
     * If app is confugured with marketplace, then fetch data from  'mp_home_page_data' file else 'home_page_data' file.
     * If there is any exception occur due to any reason, it will return null to calling function.
     * @date 23-Aug-2019 3:28 PM
     * @update 23-Aug-2019 3:28 PM
     * @author Ayushi Agarwal
     * @return either HomePageResponse or null
     */
    private HomePageResponse getHomeLocalFileData()
    {
        try {
            InputStream homePageFileData;
            if (isMarketplace) {
                homePageFileData = getAssets().open("mp_home_page_data.json");
            } else
                homePageFileData = getAssets().open("home_page_data.json");
            BufferedReader r = new BufferedReader(new InputStreamReader(homePageFileData));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }
            Gson mGson = new Gson();
            return mGson.fromJson(total.toString(), HomePageResponse.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void HitApiForHomePage() {
        Observable<SplashScreenResponse> splashScreenDataObservable = ApiConnection.getSplashPageData(this).subscribeOn(Schedulers.io());
        Observable<HomePageResponse> homePageResponseObservable = ApiConnection.getHomePageData(this).subscribeOn(Schedulers.io());

        Observable.zip(splashScreenDataObservable, homePageResponseObservable, (SplashScreenResponse splashScreenResponse, HomePageResponse homePageResponse) -> {
            splashScreenResponse.updateSharedPref(SplashScreenActivity.this);
            new SaveData(SplashScreenActivity.this, splashScreenResponse);
            if (splashScreenResponse.isAccessDenied()){
                homePageResponse.setAccessDenied(true);
            }
            if (homePageResponse.isAccessDenied()){
                homePageResponse.setAccessDenied(true);
            }
            return homePageResponse;
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<HomePageResponse>(this) {
            @Override
            public void onNext(@NonNull HomePageResponse homePageResponse) {
                super.onNext(homePageResponse);
                if (homePageResponse.isSuccess()) {
                    new SaveData(SplashScreenActivity.this, homePageResponse);
                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
                    startActivity(intent);
                } else {

                    if (homePageResponse.isAccessDenied()){
                        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(SplashScreenActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                AppSharedPref.clearCustomerData(SplashScreenActivity.this);
                                Intent i = new Intent(SplashScreenActivity.this, SignInSignUpActivity.class);
                                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SplashScreenActivity.class.getSimpleName());
                                getSplashPageData(i);
                            }
                        });
                    }else {

                        AlertDialogHelper.showDefaultWarningDialog(SplashScreenActivity.this, getString(R.string.error_login_failure), homePageResponse.getMessage());
                    }


                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
                mBinding.mainProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                super.onComplete();
            }
        });
    }

    public void getSplashPageData(Intent intent) {
        ApiConnection.getSplashPageData(this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<SplashScreenResponse>(this) {

                    @Override
                    public void onNext(@NonNull SplashScreenResponse splashScreenResponse) {
                        super.onNext(splashScreenResponse);
                        /* SAVE/UPDATE GLOBAL DATA */
                        splashScreenResponse.updateSharedPref(SplashScreenActivity.this);
                        new SaveData(SplashScreenActivity.this, splashScreenResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (!NetworkHelper.isNetworkAvailable(SplashScreenActivity.this)){
                            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(SplashScreenActivity.this);
                            SplashScreenResponse dbResponse =  sqlLiteDbHelper.getSplashScreenData();
                            if (dbResponse == null){
                                super.onError(e);
                            }else {
                                onComplete();
                            }
                        }else {
                            super.onError(e);
                        }
                        mBinding.mainProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        startActivity(intent);
                    }
                });
    }

    private Intent getNotificationIntent() {
        Intent intent = null;
        try {
            //noinspection ConstantConditions
            switch (getIntent().getExtras().getString("type")) {
                case TYPE_CUSTOM:
                    intent = new Intent(SplashScreenActivity.this, CatalogProductActivity.class);
                    intent.putExtra(BUNDLE_KEY_SEARCH_DOMAIN, getIntent().getExtras().getString("domain"));
                    intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, getIntent().getExtras().getString("name"));
                    intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, SEARCH_DOMAIN);
                    break;

                case TYPE_PRODUCT:
                    intent = new Intent(SplashScreenActivity.this, ((OdooApplication) getApplication()).getProductActivity());
                    intent.putExtra(BUNDLE_KEY_PRODUCT_ID, getIntent().getExtras().getString("id"));
                    intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, getIntent().getExtras().getString("name"));
                    break;

                case TYPE_CATEGORY:
                    intent = new Intent(SplashScreenActivity.this, CatalogProductActivity.class);
                    intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.GENERAL_CATEGORY);
                    intent.putExtra(BUNDLE_KEY_CATEGORY_ID, getIntent().getExtras().getString("id"));
                    intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, getIntent().getExtras().getString("name"));
                    break;
                case TYPE_NONE:

                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return intent;

    }
}
