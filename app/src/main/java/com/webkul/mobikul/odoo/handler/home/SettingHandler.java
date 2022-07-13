package com.webkul.mobikul.odoo.handler.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.ActivitySettingsBinding;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.ErrorConstants;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SettingHandler {
    private ActivitySettingsBinding binding;
    private Context context;
    private SweetAlertDialog mSweetAlertDialog;
    private String source;

    public SettingHandler(ActivitySettingsBinding mBinding, Context context) {
        this.binding = mBinding;
        this.context = context;
        source = "Setting";
    }

    public String getCurrentVersion() {
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return "App version - " + pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void onPrivacyPolicyClick() {
        AnalyticsImpl.INSTANCE.trackPrivacyPolicyClick(source);
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppSharedPref.getPrivacyURL(context)));
        List<Intent> intents = new ArrayList<>();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);

        for (ResolveInfo info : list) {
            Intent viewIntent = new Intent(intent);
            viewIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            viewIntent.setPackage(info.activityInfo.packageName);
            intents.add(viewIntent);
        }

        for (Intent cur : intents) {
            if (cur.getComponent().getClassName().equalsIgnoreCase("com.webkul.mobikul.odoo.activity.SplashScreenActivity")) {
                intents.remove(cur);
            }
        }

        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[intents.size()]));
        ((Activity) context).startActivity(intent);
    }

    public void onClickThemeIcon() {
//        String themePreference = "isDark";
        int isDark = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (Configuration.UI_MODE_NIGHT_NO == isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            AppSharedPref.setDarkMode(context, true);
//            BaseActivity.setLocale(context, true);
        }
        if (Configuration.UI_MODE_NIGHT_YES == isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            AppSharedPref.setDarkMode(context, false);
//            BaseActivity.setLocale(context, true);
        }
        AppSharedPref.setIsDarkChange(context, true);
    }

    public void oNClickedClearRecentView() {
        mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.setTitleText(context.getString(R.string.clearrecent))
                .setContentText(context.getString(R.string.reventviewedproduct))
                .setConfirmText(context.getString(R.string.ok))
                .setConfirmClickListener(sDialog -> {
                    SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
                    try {
                        AnalyticsImpl.INSTANCE.trackClearRecentViewProductListSuccess();
                        sqlLiteDbHelper.deleteAllProductData();
                    } catch (Exception e) {
                        AnalyticsImpl.INSTANCE.trackClearRecentViewProductListFailed(ErrorConstants.ClearRecentViewError.INSTANCE.getErrorCode(), ErrorConstants.ClearRecentViewError.INSTANCE.getErrorMessage());
                    }
                    mSweetAlertDialog.dismiss();
                })
                .setCancelText(context.getString(R.string.cancel_small))
                .setCancelClickListener(Dialog::dismiss)
                .show();
    }

    public void onClickedShowRecentView() {
        try {
            if (binding.scShowRecentView.isChecked()) {
                AppSharedPref.setRecentViewEnable(context, true);
                AnalyticsImpl.INSTANCE.trackShowRecentViewProductListToggleSuccess(true);

            } else {
                AppSharedPref.setRecentViewEnable(context, false);
                AnalyticsImpl.INSTANCE.trackShowRecentViewProductListToggleSuccess(false);
            }
        } catch (Exception e) {
            AnalyticsImpl.INSTANCE.trackShowRecentViewProductListToggleFailed(ErrorConstants.RecentViewToggleError.INSTANCE.getErrorCode(), ErrorConstants.RecentViewToggleError.INSTANCE.getErrorMessage());
        }

    }

    public void onClickedClearSearchHistory() {
        mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.setTitleText(context.getString(R.string.clearsearch))
                .setContentText(context.getString(R.string.clearsearchhistory))
                .setConfirmText(context.getString(R.string.ok))
                .setConfirmClickListener(sDialog -> {
                    SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
                    try {
                        AnalyticsImpl.INSTANCE.trackClearSearchHistorySuccess();
                        sqlLiteDbHelper.deleteAllSearchData();
                    } catch (Exception e) {
                        AnalyticsImpl.INSTANCE.trackClearSearchHistoryFailed(ErrorConstants.ClearSearchHistoryError.INSTANCE.getErrorCode(), ErrorConstants.ClearSearchHistoryError.INSTANCE.getErrorMessage());
                    }
                    mSweetAlertDialog.dismiss();
                })
                .setCancelText(context.getString(R.string.cancel_small))
                .setCancelClickListener(Dialog::dismiss)
                .show();
    }

    public void onClickNotification() {
        gotoNotificationPage();
    }

    private void gotoNotificationPage() {
        try {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", context.getPackageName());
                intent.putExtra("app_uid", context.getApplicationInfo().uid);
            } else {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
            }

            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                AnalyticsImpl.INSTANCE.trackShowNotificationToggleSuccess(true);
            } else {
                AnalyticsImpl.INSTANCE.trackShowNotificationToggleSuccess(false);
            }

            context.startActivity(intent);
        } catch (Exception e) {
            AnalyticsImpl.INSTANCE.trackShowNotificationToggleFailed(ErrorConstants.NotificationToggleError.INSTANCE.getErrorCode(), ErrorConstants.NotificationToggleError.INSTANCE.getErrorMessage());
        }
    }

}
