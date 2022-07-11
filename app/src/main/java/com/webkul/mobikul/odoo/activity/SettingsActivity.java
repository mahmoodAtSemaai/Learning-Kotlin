package com.webkul.mobikul.odoo.activity;

import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.Menu;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ActivitySettingsBinding;
import com.webkul.mobikul.odoo.handler.home.SettingHandler;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;

public class SettingsActivity extends BaseActivity {
    private ActivitySettingsBinding binding;
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppSharedPref.isDarkChange(this)) {
            AppSharedPref.setIsDarkChange(this, false);
            BaseActivity.setLocale(this, false);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setSupportActionBar(binding.toolbar);
        setActionbarTitle(this.getResources().getString(R.string.setting));
        showBackButton(true);
        setDataOnToggle();
        binding.setHandler(new SettingHandler(binding, this));
        if (AppSharedPref.isDarkMode(this))
            binding.tvTheme.setText(R.string.lightMode);
        else
            binding.tvTheme.setText(R.string.darkMode);
    }

    private void setDataOnToggle() {

        if (AppSharedPref.isRecentViewEnable(this)) {
            binding.scShowRecentView.setChecked(true);
        } else {
            binding.scShowRecentView.setChecked(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            binding.scShowNotification.setChecked(true);
        } else {
            binding.scShowNotification.setChecked(false);
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