package com.webkul.mobikul.odoo.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.Menu;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ActivityOrderPlacedBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.fragment.OrderFragment;
import com.webkul.mobikul.odoo.handler.checkout.OrderPlaceActivityHandler;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_ORDER_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_URL;
/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class OrderPlacedActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderPlacedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOrderPlacedBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_order_placed);
        setSupportActionBar(binding.toolbar);
        showBackButton(true);
        binding.setOrderName(getIntent().getStringExtra(BUNDLE_KEY_ORDER_NAME));
        binding.setHandler(new OrderPlaceActivityHandler(this, getIntent().getStringExtra(BUNDLE_KEY_URL)));
        mSupportFragmentManager.addOnBackStackChangedListener(this);
        FirebaseAnalyticsImpl.logEcommercePurchase(this,getIntent().getStringExtra(BUNDLE_KEY_ORDER_NAME));
    }

    /*Create no menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /*Redirect only to Home page */

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackStackChanged() {
        String tag = mSupportFragmentManager.getBackStackEntryAt(mSupportFragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment fragment = mSupportFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            return;
        }
        if (fragment instanceof OrderFragment) {
            setTitle(getString(R.string.my_order));
        } else {
            setTitle(R.string.title_activity_order_placed);
        }
    }
}