package com.webkul.mobikul.odoo.handler.home;

import static com.webkul.mobikul.odoo.BuildConfig.APP_PLAYSTORE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.LoyaltyHistoryActivity;
import com.webkul.mobikul.odoo.activity.SettingsActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.dialog_frag.RateAppDialogFragm;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.ErrorConstants;
import com.webkul.mobikul.odoo.helper.OdooApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubham.agarwal on 27/5/17.
 */

public class HomeActivityHandler {

    @SuppressWarnings("unused")
    private static final String TAG = "HomeActivityHandler";
    private Context mContext;
    private AlertDialog languageDialog;

    public HomeActivityHandler(Context context) {
        mContext = context;
    }

    public void rateUs() {
        RateAppDialogFragm.newInstance().show(((BaseActivity) mContext).mSupportFragmentManager, RateAppDialogFragm.class.getSimpleName());
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, APP_PLAYSTORE_URL);
        sendIntent.setType("text/plain");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mContext.startActivity(Intent.createChooser(sendIntent, mContext.getString(R.string.choose_an_action), null));
        } else {
            mContext.startActivity(sendIntent);
        }
    }

    public void onClickMarketplaceIcon() {
        String SourceScreen = mContext.getString(R.string.sourceScreen);
        AnalyticsImpl.INSTANCE.trackMarketPlaceClick(SourceScreen);
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getMarketplaceLandingPage());
        mContext.startActivity(intent);
    }

    public void onClickThemeIcon() {
        String themePreference = "isDark";
        int isDark = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        SharedPreferences.Editor editor = AppSharedPref.getSharedPreferenceEditor(mContext, themePreference);
        if (Configuration.UI_MODE_NIGHT_NO == isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("DARK", true);
            editor.apply();
            editor.commit();
        }
        if (Configuration.UI_MODE_NIGHT_YES == isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("DARK", false);
            editor.apply();
            editor.commit();
        }
    }

    public void onClickLanguageIcon(String  lang_code) {

        AnalyticsImpl.INSTANCE.trackLanguageClick(lang_code);

                try {
                        AppSharedPref.setLanguageCode(mContext, lang_code);
                        AppSharedPref.setIsLanguageChange(mContext, true);
                        AnalyticsImpl.INSTANCE.trackLanguageChangeSuccess(lang_code);
                        // delete product data

                        SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(mContext);
                        sqlLiteDbHelper.deleteAllProductData();

                        if (languageDialog != null) {
                            languageDialog.dismiss();
                        }
                        BaseActivity.setLocale(mContext, true);

                } catch (Exception e) {
                    AnalyticsImpl.INSTANCE.trackLanguageChangeFail(lang_code, ErrorConstants.LanguageChangeError.INSTANCE.getErrorCode(), ErrorConstants.LanguageChangeError.INSTANCE.getErrorMessage());
                }

    }

    public void onClickSettings() {
        Intent intent = new Intent(mContext, SettingsActivity.class);
        mContext.startActivity(intent);

    }

    public void showPointsHistory() {
        Intent intent = new Intent(mContext, LoyaltyHistoryActivity.class);
        mContext.startActivity(intent);
    }
}
