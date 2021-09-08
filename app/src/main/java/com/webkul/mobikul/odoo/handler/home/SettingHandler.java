package com.webkul.mobikul.odoo.handler.home;

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

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.ActivitySettingsBinding;
import com.webkul.mobikul.odoo.helper.AppSharedPref;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SettingHandler {
    private ActivitySettingsBinding mBinding;
    private Context context;
    private SweetAlertDialog mSweetAlertDialog;

    public SettingHandler(ActivitySettingsBinding mBinding, Context context) {
        this.mBinding = mBinding;
        this.context = context;
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
        context.startActivity(intent);
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
                    sqlLiteDbHelper.deleteAllProductData();
                    mSweetAlertDialog.dismiss();
                })
                .setCancelText(context.getString(R.string.cancel_small))
                .setCancelClickListener(Dialog::dismiss)
                .show();
    }

    public void onClickedShowRecentView() {
        if (mBinding.showRecentView.isChecked()) {
            System.out.println("TestingShowRecentView==> Checked");
            AppSharedPref.setRecentViewEnable(context, true);

        } else {
            System.out.println("TestingShowRecentView==> UnChecked");
            AppSharedPref.setRecentViewEnable(context, false);
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
                    sqlLiteDbHelper.deleteAllSearchData();
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
        context.startActivity(intent);
    }

}
