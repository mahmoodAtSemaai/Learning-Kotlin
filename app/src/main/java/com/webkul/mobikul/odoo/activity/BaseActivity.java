package com.webkul.mobikul.odoo.activity;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.RetrofitClient;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.IntentHelper;
import com.webkul.mobikul.odoo.updates.ForceUpdateManager;

import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.disposables.CompositeDisposable;


public abstract class BaseActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    public static final String TAG = "BaseActivity";
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    public SweetAlertDialog mSweetAlertDialog;
    public FragmentManager mSupportFragmentManager;
    public SQLiteDatabase mSqLiteDatabase;
    private MenuItem mItemBag;

    public static void setLocale(Context ctx, boolean reload) {
        String langCode = AppSharedPref.getLanguageCode(ctx);
        Locale locale;
        if (langCode.contains("_"))
            langCode = langCode.split("_")[0];
        if (langCode.equals("id")) {
            locale = new Locale("id", "ID");
        } else {
            locale = new Locale(langCode);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        Log.d(TAG, "setLocale: " + locale.toString());
        ((AppCompatActivity) ctx).getBaseContext().getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());

        if (reload) {
            Intent intent = new Intent(ctx, SplashScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
            ((BaseActivity) ctx).finish();
        }
    }

    public abstract String getScreenTitle();

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale(this, false);
        mSupportFragmentManager = getSupportFragmentManager();
        SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(this);
        mSqLiteDatabase = sqlLiteDbHelper.getWritableDatabase();
        if (!(this instanceof SplashScreenActivity) && !(this instanceof CheckoutActivity)
                && !(this instanceof ProductActivity)) {
            ForceUpdateManager.init(this);
        }
        trackActivityOpened();

    }

    protected void showBackButton(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }

    protected void setActionbarTitle(@Nullable String title) {
//        if (title == null) {
//            return;
//        }
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(title);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem wishlistMenuItem = menu.findItem(R.id.menu_item_wishlist);
        if (AppSharedPref.isAllowedWishlist(this) && AppSharedPref.isLoggedIn(this)) {
            wishlistMenuItem.setVisible(true);
        } else {
            wishlistMenuItem.setVisible(false);
        }
        mItemBag = menu.findItem(R.id.menu_item_bag);
        LayerDrawable icon = (LayerDrawable) mItemBag.getIcon();
        Helper.setBadgeCount(this, icon, AppSharedPref.getCartCount(this, 0));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
        }
        // all [else] codeblock shifted to @HomeActivity @CustomerBaseActivity for code uniformity & analytics
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            Helper.hideKeyboard(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onBackPressed: ****");
        if (getIntent().getExtras() != null && getIntent().hasExtra(BUNDLE_KEY_CALLING_ACTIVITY)) {
            String callingActivity = getIntent().getExtras().getString(BUNDLE_KEY_CALLING_ACTIVITY);
            if (callingActivity != null && (callingActivity.equals(SplashScreenActivity.class.getSimpleName()))) {
                IntentHelper.continueShopping(this);
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mItemBag != null) {
            LayerDrawable icon = (LayerDrawable) mItemBag.getIcon();
            Helper.setBadgeCount(this, icon, AppSharedPref.getCartCount(this, 0));
        }
    }

    public void updateCartBadge(int cartCount) {
        AppSharedPref.setCartCount(this, cartCount);
        if (mItemBag != null) {
            LayerDrawable icon = (LayerDrawable) mItemBag.getIcon();
            Helper.setBadgeCount(this, icon, AppSharedPref.getCartCount(this, 0));
        }
    }

    public void updateEmailVerification(boolean isEmailVerified) {
        Log.d(TAG, "updateEmailVerification: " + isEmailVerified);
        boolean isEmailVerifiedSaved = AppSharedPref.isEmailVerified(this);
        Log.d(TAG, "updateEmailVerification: " + isEmailVerifiedSaved);
        if (isEmailVerified != isEmailVerifiedSaved) {
            AppSharedPref.setEmailVerified(this, isEmailVerified);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCompositeDisposable.clear();
        RetrofitClient.getDispatcher().cancelAll();
        trackActivityClosed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSqLiteDatabase.close();
        AlertDialogHelper.dismiss(this);

    }

    private void trackActivityOpened() {
        if ((this instanceof BagActivity || this instanceof CatalogProductActivity ||
                this instanceof ProductActivity || this instanceof SettingsActivity ||
                this instanceof SplashScreenActivity || this instanceof SubCategoryActivity ||
                this instanceof UpdateAddressActivity || this instanceof UserApprovalActivity) &&
                !getScreenTitle().equals(TAG))
            AnalyticsImpl.INSTANCE.trackActivityOpened(Helper.getScreenName(getScreenTitle()));
    }

    private void trackActivityClosed() {
        if ((this instanceof BagActivity || this instanceof CatalogProductActivity ||
                this instanceof ProductActivity || this instanceof SettingsActivity ||
                this instanceof SplashScreenActivity || this instanceof SubCategoryActivity ||
                this instanceof UpdateAddressActivity || this instanceof UserApprovalActivity) &&
                !getScreenTitle().equals(TAG))
            AnalyticsImpl.INSTANCE.trackActivityClosed(Helper.getScreenName(getScreenTitle()));
    }

}