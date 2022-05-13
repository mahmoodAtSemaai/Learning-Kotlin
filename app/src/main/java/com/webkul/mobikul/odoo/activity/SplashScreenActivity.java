package com.webkul.mobikul.odoo.activity;

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
import static com.webkul.mobikul.odoo.helper.CatalogHelper.CatalogProductRequestType.SEARCH_DOMAIN;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.database.SaveData;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.ActivitySplashScreenBinding;
import com.webkul.mobikul.odoo.features.auth.presentation.SignInSignUpActivityV1;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.IntentHelper;
import com.webkul.mobikul.odoo.helper.NetworkHelper;
import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.model.analytics.UserAnalyticsResponse;
import com.webkul.mobikul.odoo.model.extra.SplashScreenActivityData;
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;
import com.webkul.mobikul.odoo.model.user.UserModel;
import com.webkul.mobikul.odoo.updates.AppUpdateHelper;

import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


public class SplashScreenActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "SplashActivity";
    private static final int RC_UPDATE_APP_FROM_PLAYSTORE = 1;
    CustomObserver<SplashScreenResponse> splashSubscriber;
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
        if (AppSharedPref.getUserAnalyticsId(this) == null && AppSharedPref.isLoggedIn(SplashScreenActivity.this)) {

            ApiConnection.getUserAnalytics(this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<UserAnalyticsResponse>(this) {
                @Override
                public void onNext(@androidx.annotation.NonNull UserAnalyticsResponse userAnalyticsResponse) {

                    AnalyticsImpl.INSTANCE.initUserTracking(new UserModel(
                            userAnalyticsResponse.getEmail(),
                            userAnalyticsResponse.getAnalyticsId(),
                            userAnalyticsResponse.getName(),
                            userAnalyticsResponse.isSeller()
                    ));
                    AppSharedPref.setUserAnalyticsId(getBaseContext(), userAnalyticsResponse.getAnalyticsId());
                    callApi();
                    super.onNext(userAnalyticsResponse);
                }

                @Override
                public void onError(@androidx.annotation.NonNull Throwable t) {
                    super.onError(t);
                    AnalyticsImpl.INSTANCE.trackAnalyticsFailure();
                    callApi();
                }
            });
        } else {
            int darkModeFlag = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            boolean isDark = AppSharedPref.isDarkMode(this);
            if (isDark || darkModeFlag == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                AppSharedPref.setDarkMode(this, true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            Helper.logIntentBundleData(getIntent());
            mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
            mBinding.setData(new SplashScreenActivityData());    // starting progressBar
            FirebaseAnalyticsImpl.logAppOpenEvent(this);

            callApi();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_UPDATE_APP_FROM_PLAYSTORE) {
            callApi();
        }
    }


    private void callApi() {

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
            if (NetworkHelper.isNetworkAvailable(this)) {
                initSplashScreenAPI();
            } else {
                SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(SplashScreenActivity.this);
                HomePageResponse homePageResponse = sqlLiteDbHelper.getHomeScreenData();
                if (homePageResponse != null) {

                    directToNewHomeActivity(homePageResponse);

                } else {
                    initSplashScreenAPI();
                }
            }
            /*adding subscribe on here instead of zip to create the observable on io thread. */

        } else {
            Intent i;
            if (AppUpdateHelper.getAuthRevampEnabled()) {
                i = new Intent(SplashScreenActivity.this, SignInSignUpActivityV1.class);
            } else {
                i = new Intent(SplashScreenActivity.this, SignInSignUpActivity.class);
            }
            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SplashScreenActivity.class.getSimpleName());
            getSplashPageData(i);
        }
    }

    private void directToNewHomeActivity(HomePageResponse homePageResponse) {
        Intent intent = new Intent(SplashScreenActivity.this, NewHomeActivity.class);
        intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
        startActivity(intent);
    }

    private void HitApiForHomePage() {
        Observable<SplashScreenResponse> splashScreenDataObservable = ApiConnection.getSplashPageData(this).subscribeOn(Schedulers.io());
        Observable<HomePageResponse> homePageResponseObservable = ApiConnection.getHomePageData(this).subscribeOn(Schedulers.io());

        Observable.zip(splashScreenDataObservable, homePageResponseObservable, (SplashScreenResponse splashScreenResponse, HomePageResponse homePageResponse) -> {
            splashScreenResponse.updateSharedPref(SplashScreenActivity.this);
            new SaveData(SplashScreenActivity.this, splashScreenResponse);
            if (splashScreenResponse.isAccessDenied()) {
                homePageResponse.setAccessDenied(true);
            }
            if (homePageResponse.isAccessDenied()) {
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

                    if (homePageResponse.isAccessDenied()) {
                        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(SplashScreenActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                AppSharedPref.clearCustomerData(SplashScreenActivity.this);
                                Intent i = new Intent(SplashScreenActivity.this, SignInSignUpActivityV1.class);
                                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SplashScreenActivity.class.getSimpleName());
                                getSplashPageData(i);
                            }
                        });
                    } else {

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

    public void initSplashScreenAPI() {
        ApiConnection.getSplashPageData(this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<SplashScreenResponse>(this) {
                    @Override
                    public void onNext(@androidx.annotation.NonNull SplashScreenResponse splashScreenResponse) {
                        super.onNext(splashScreenResponse);
                        if (!splashScreenResponse.isUserApproved()) {
                            // user not approved => redirect to user unapproved screen
                            IntentHelper.goToUserUnapprovedScreen(SplashScreenActivity.this);
                            finish();
                        } else {
                            // approved => continue as usual
                            initHomeScreenAPI(splashScreenResponse);
                        }
                    }

                    @Override
                    public void onError(@androidx.annotation.NonNull Throwable t) {
                        super.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    public void initHomeScreenAPI(SplashScreenResponse splashScreenResponse) {
        ApiConnection.getHomePageData(this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<HomePageResponse>(this) {
                    @Override
                    public void onNext(@androidx.annotation.NonNull HomePageResponse homePageResponse) {
                        super.onNext(homePageResponse);
                        if (splashScreenResponse.isAccessDenied()) {
                            homePageResponse.setAccessDenied(true);
                        }
                        if (homePageResponse.isAccessDenied()) {
                            homePageResponse.setAccessDenied(true);
                        }
                        if (homePageResponse.isSuccess()) {
                            new SaveData(SplashScreenActivity.this, homePageResponse);
                            Intent intent = new Intent(SplashScreenActivity.this, NewHomeActivity.class);
                            intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
                            startActivity(intent);
                        } else {

                            if (homePageResponse.isAccessDenied()) {
                                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(SplashScreenActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        AppSharedPref.clearCustomerData(SplashScreenActivity.this);
                                        Intent i = new Intent(SplashScreenActivity.this, SignInSignUpActivityV1.class);
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SplashScreenActivity.class.getSimpleName());
                                        getSplashPageData(i);
                                    }
                                });
                            } else {

                                AlertDialogHelper.showDefaultWarningDialog(SplashScreenActivity.this, getString(R.string.error_login_failure), homePageResponse.getMessage());
                            }


                        }
                    }

                    @Override
                    public void onError(@androidx.annotation.NonNull Throwable t) {
                        super.onError(t);
                        mBinding.mainProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    public void getSplashPageData(Intent intent) {

        splashSubscriber = new CustomObserver<SplashScreenResponse>(this) {

            @Override
            public void onNext(@NonNull SplashScreenResponse splashScreenResponse) {
                super.onNext(splashScreenResponse);
                /* SAVE/UPDATE GLOBAL DATA */
                splashScreenResponse.updateSharedPref(SplashScreenActivity.this);
                new SaveData(SplashScreenActivity.this, splashScreenResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {

                if (!NetworkHelper.isNetworkAvailable(SplashScreenActivity.this)) {
                    SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(SplashScreenActivity.this);
                    SplashScreenResponse dbResponse = sqlLiteDbHelper.getSplashScreenData();
                    if (dbResponse == null) {
                        super.onError(e);
                    } else {
                        onComplete();
                    }
                } else {
                    super.onError(e);
                }
                mBinding.mainProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                startActivity(intent);
            }
        };

        ApiConnection.getSplashPageData(this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(splashSubscriber);
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

    @Override
    public String getScreenTitle() {
        return TAG;
    }
}
