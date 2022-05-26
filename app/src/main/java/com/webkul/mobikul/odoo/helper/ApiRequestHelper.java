package com.webkul.mobikul.odoo.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.NewHomeActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.webkul.mobikul.odoo.BuildConfig.isMarketplace;
import static com.webkul.mobikul.odoo.activity.ProductActivity.RC_ADD_TO_CART;
import static com.webkul.mobikul.odoo.activity.ProductActivity.RC_BUY_NOW;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_REQ_CODE;

/**
 * Created by shubham.agarwal on 14/6/17.
 */

public class ApiRequestHelper {



    public static void callHomePageApi(Context context) {
        ApiConnection.getHomePageData(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<HomePageResponse>(context) {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
              //  AlertDialogHelper.showDefaultProgressDialog(context);
            }

            @Override
            public void onNext(@NonNull HomePageResponse homePageResponse) {
                super.onNext(homePageResponse);
                homePageResponse.updateSharedPref(context, "");
                if (((Activity) context).getIntent().hasExtra(BUNDLE_KEY_REQ_CODE)) {
                    switch (((Activity) context).getIntent().getExtras().getInt(BUNDLE_KEY_REQ_CODE)) {
                        case RC_ADD_TO_CART:
                            Intent addToCartReturnIntent = new Intent();
                            addToCartReturnIntent.putExtra(BUNDLE_KEY_REQ_CODE, RC_ADD_TO_CART);
                            ((Activity) context).setResult(RESULT_OK, addToCartReturnIntent);
                            ((Activity) context).finish();
                            break;
                        case RC_BUY_NOW:
                            Intent buyNowReturnIntent = new Intent();
                            buyNowReturnIntent.putExtra(BUNDLE_KEY_REQ_CODE, RC_BUY_NOW);
                            ((Activity) context).setResult(RESULT_OK, buyNowReturnIntent);
                            ((Activity) context).finish();
                            break;
                    }
                    return;
                }

                Intent intent = new Intent(context, NewHomeActivity.class);
                intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
                context.startActivity(intent);
                ((Activity) context).finish();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);

            }

            @Override
            public void onComplete() {

            }
        });
    }



    /**
     * If user opens app first time & there is no extra request for this method(BUNDLE_KEY_REQ_CODE), then get home page data from local file and pass data to homeActivity
     * Else call callHomeApi(context) method.
     * @date 23-Aug-2019 3:28 PM
     * @update 23-Aug-2019 3:28 PM
     * @author Ayushi Agarwal
     * @param context context of calling activity
     */
    public static void callHomePageApi_(Context context) {
        if (AppSharedPref.getLanguageCode(context).equals("") || AppSharedPref.isAppRunFirstTime(context)) {
            AppSharedPref.setIsAppRunFirstTime(context, false);
            if (!((Activity) context).getIntent().hasExtra(BUNDLE_KEY_REQ_CODE)) {
                HomePageResponse homePageResponse = getLoacalFileData(context);
                if (homePageResponse != null) {
                    homePageResponse.updateSharedPref(context, "");
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    return;
                }
            }
        }
        callHomeApi(context);
    }

    /**
     * Method is used to call home api.
     * Need a context of activity to call this method.
     * If calling activity having intent request of Add_TO_CART or BUY_NOW, then after getting the response, this method simply return back control to requested activity of Add_TO_CART or BUY_NOW.
     * Else this method will call homeActivity with homeResponse and finish calling activity.
     * @update 23-Aug-2019 3:28 PM
     * @author Ayushi Agarwal
     * @param context context of calling activity
     */
    private static void callHomeApi(Context context) {
        ApiConnection.getHomePageData(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<HomePageResponse>(context) {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(context);
            }

            @Override
            public void onNext(@NonNull HomePageResponse homePageResponse) {
                super.onNext(homePageResponse);
                homePageResponse.updateSharedPref(context, "");
                if (((Activity) context).getIntent().hasExtra(BUNDLE_KEY_REQ_CODE)) {
                    switch (((Activity) context).getIntent().getExtras().getInt(BUNDLE_KEY_REQ_CODE)) {
                        case RC_ADD_TO_CART:
                            Intent addToCartReturnIntent = new Intent();
                            addToCartReturnIntent.putExtra(BUNDLE_KEY_REQ_CODE, RC_ADD_TO_CART);
                            ((Activity) context).setResult(RESULT_OK, addToCartReturnIntent);
                            ((Activity) context).finish();
                            break;
                        case RC_BUY_NOW:
                            Intent buyNowReturnIntent = new Intent();
                            buyNowReturnIntent.putExtra(BUNDLE_KEY_REQ_CODE, RC_BUY_NOW);
                            ((Activity) context).setResult(RESULT_OK, buyNowReturnIntent);
                            ((Activity) context).finish();
                            break;
                    }
                    return;
                }
                Intent intent = new Intent(context, NewHomeActivity.class);
                intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
                context.startActivity(intent);
                ((Activity) context).finish();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);

            }

            @Override
            public void onComplete() {

            }
        });
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
    private static HomePageResponse getLoacalFileData(Context context) {
        try {
            InputStream homePageFileData;
            if (isMarketplace) {
                homePageFileData = context.getAssets().open("mp_home_page_data.json");
            } else
                homePageFileData = context.getAssets().open("home_page_data.json");
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

}
