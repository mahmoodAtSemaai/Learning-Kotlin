package com.webkul.mobikul.odoo.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.product.AlternativeProductsRvAdapter;
import com.webkul.mobikul.odoo.database.SearchHistoryContract;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.ActivityProductBinding;
import com.webkul.mobikul.odoo.databinding.ActivitySettingsBinding;
import com.webkul.mobikul.odoo.handler.home.SettingHandler;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CustomerHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.generic.ProductData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CUSTOMER_FRAG_TYPE;
import static com.webkul.mobikul.odoo.database.SearchHistoryContract.SearchHistoryEntry.TABLE_NAME;

public class SettingsActivity extends BaseActivity {
    private ActivitySettingsBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppSharedPref.isDarkChange(this)) {
            AppSharedPref.setIsDarkChange(this, false);
            BaseActivity.setLocale(this, false);
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setSupportActionBar(mBinding.toolbar);
        setActionbarTitle(this.getResources().getString(R.string.setting));
        showBackButton(true);
        setDataOnToggle();
        mBinding.setHandler(new SettingHandler(mBinding, this));
        if (AppSharedPref.isDarkMode(this))
            mBinding.themeTextView.setText(R.string.lightMode);
        else
            mBinding.themeTextView.setText(R.string.darkMode);
    }

    private void setDataOnToggle() {

        if (AppSharedPref.isRecentViewEnable(this)) {
            mBinding.showRecentView.setChecked(true);
        } else {
            mBinding.showRecentView.setChecked(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            mBinding.showNotification.setChecked(true);
        } else {
            mBinding.showNotification.setChecked(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Helper.hiderAllMenuItems(menu);
        return true;
    }

    @Override
    public String getScreenTitle() {
        return TAG;
    }

}