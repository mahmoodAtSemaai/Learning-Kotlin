package com.webkul.mobikul.odoo.activity;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.View;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ActivityCheckoutBinding;
import com.webkul.mobikul.odoo.fragment.BillingShippingFragment;
import com.webkul.mobikul.odoo.fragment.NewAddressFragment;
import com.webkul.mobikul.odoo.fragment.OrderReviewFragment;
import com.webkul.mobikul.odoo.fragment.PaymentAcquirerFragment;
import com.webkul.mobikul.odoo.fragment.ShippingMethodFragment;
import com.webkul.mobikul.odoo.handler.checkout.CheckoutActivityHandler;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.checkout.CheckoutActivityData;

/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class CheckoutActivity extends BaseLocationActivity implements FragmentManager.OnBackStackChangedListener {
    private ActivityCheckoutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);
        setSupportActionBar(mBinding.toolbar);
        showBackButton(true);
        mBinding.setData(new CheckoutActivityData());
        mBinding.setHandler(new CheckoutActivityHandler(this));
        mSupportFragmentManager.addOnBackStackChangedListener(this);
        FragmentHelper.replaceFragment(R.id.container, this, BillingShippingFragment.newInstance(), BillingShippingFragment.class.getSimpleName(), true, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        Helper.hideKeyboard(CheckoutActivity.this);
        Fragment fragment = mSupportFragmentManager.findFragmentByTag(NewAddressFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mBinding.toolbar.setTitle(title);
    }

    @Override
    public void onBackStackChanged() {

        String tag = mSupportFragmentManager.getBackStackEntryAt(mSupportFragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment fragment = mSupportFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            return;
        }

        if (fragment instanceof NewAddressFragment) {
            mBinding.topBar.setVisibility(View.GONE);
            return;
        }

        mBinding.topBar.setVisibility(View.VISIBLE);// DEFAULT


        if (fragment instanceof BillingShippingFragment) {
            setActionbarTitle(getString(R.string.set_address));
            mBinding.getData().setPaymentMethodClickable(false);
            mBinding.getData().setOrderReviewClickable(false);
            mBinding.getData().setShippingMethodClickable(false);
            return;
        }

        if (fragment instanceof ShippingMethodFragment) {
            setActionbarTitle(getString(R.string.checkout_heading_shipping_method));
            mBinding.getData().setShippingMethodClickable(true);
            mBinding.getData().setPaymentMethodClickable(false);
            mBinding.getData().setOrderReviewClickable(false);
            return;
        }

        if (fragment instanceof PaymentAcquirerFragment) {
            setActionbarTitle(getString(R.string.select_payment_method));
            mBinding.getData().setShippingMethodClickable(true);
            mBinding.getData().setPaymentMethodClickable(true);
            mBinding.getData().setOrderReviewClickable(false);
            return;
        }

        if (fragment instanceof OrderReviewFragment) {
            setActionbarTitle(getString(R.string.review_order));
            mBinding.getData().setShippingMethodClickable(true);
            mBinding.getData().setPaymentMethodClickable(true);
            mBinding.getData().setOrderReviewClickable(true);
            //noinspection UnnecessaryReturnStatement
            return;

        }
    }

}
