package com.webkul.mobikul.odoo.connection;


import android.annotation.SuppressLint;
import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.helper.AppSharedPref;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class RetrofitClient {

    @SuppressWarnings("unused")
    private static final String TAG = "RetrofitClient";
    private static final int DEFAULT_CONNECT_TIMEOUT_IN_SEC = 90;
    private static final int DEFAULT_WRITE_TIMEOUT_IN_SEC = 90;
    private static final int DEFAULT_READ_TIMEOUT_IN_SEC = 90;

    private static Retrofit sRetrofit = null;
    private static Dispatcher sDispatcher = null;
    @SuppressLint("StaticFieldLeak")
    private static Context sContext; // this will not leak.

    @SuppressWarnings("WeakerAccess")
    public static Retrofit getClient(Context context) {
        if (sRetrofit == null) {
            sContext = context.getApplicationContext();
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClientBuilder(1).build())
                    .build();
        }
        return sRetrofit;
    }


    private static OkHttpClient.Builder getOkHttpClientBuilder(int timeoutMuliplier) {
        /*OkHttp client builder*/
        OkHttpClient.Builder oktHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(timeoutMuliplier * DEFAULT_CONNECT_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .writeTimeout(timeoutMuliplier * DEFAULT_WRITE_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .readTimeout(timeoutMuliplier * DEFAULT_READ_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .cookieJar(new JavaNetCookieJar(getCookieManager())); /* Using okhttp3 cookie instead of java net cookie*/
        oktHttpClientBuilder.dispatcher(getDispatcher());

        oktHttpClientBuilder.addInterceptor(chain -> {

            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Authorization", BuildConfig.BASIC_AUTH_KEY)
                    .addHeader("Content-Type", "text/html");
            if (AppSharedPref.isLoggedIn(sContext)) {
                if (AppSharedPref.isSocialLoggedIn(sContext)) {
                    builder.addHeader("SocialLogin", AppSharedPref.getCustomerLoginBase64Str(sContext));
                } else {
                    builder.addHeader("Login", AppSharedPref.getCustomerLoginBase64Str(sContext));
                }
            }
            if (!AppSharedPref.getLanguageCode(sContext).isEmpty()) {
                builder.addHeader("lang", AppSharedPref.getLanguageCode(sContext));
            }
            return chain.proceed(builder.build());
        });

        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            /*http request logging*/
            oktHttpClientBuilder.addInterceptor(getHttpLoggingInterceptor());
        }
        oktHttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
        return oktHttpClientBuilder;
    }

    private static CookieManager getCookieManager() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return cookieManager;
    }

    public static Dispatcher getDispatcher() {
        if (sDispatcher == null) {
            return new Dispatcher();
        }
        return sDispatcher;
    }

    private static Interceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }


}