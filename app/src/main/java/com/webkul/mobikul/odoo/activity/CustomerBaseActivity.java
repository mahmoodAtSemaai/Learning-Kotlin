package com.webkul.mobikul.odoo.activity;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.speech.RecognizerIntent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.custom.MaterialSearchView;
import com.webkul.mobikul.odoo.databinding.ActivityCustomerBaseBinding;
import com.webkul.mobikul.odoo.fragment.AccountInfoFragment;
import com.webkul.mobikul.odoo.fragment.AddressBookFragment;
import com.webkul.mobikul.odoo.fragment.DashboardFragment;
import com.webkul.mobikul.odoo.fragment.NewAddressFragment;
import com.webkul.mobikul.odoo.fragment.OrderFragment;
import com.webkul.mobikul.odoo.fragment.OrderListFragment;
import com.webkul.mobikul.odoo.fragment.WishlistFragment;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CustomerHelper;
import com.webkul.mobikul.odoo.helper.CustomerHelper.CustomerFragType;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.IntentHelper;

import java.util.ArrayList;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CUSTOMER_FRAG_TYPE;

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

public class CustomerBaseActivity extends BaseLocationActivity implements FragmentManager.OnBackStackChangedListener {

    @SuppressWarnings("unused")
    private static final String TAG = "CustomerBaseActivity";

    public ActivityCustomerBaseBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_base);
        setSupportActionBar(mBinding.toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSupportFragmentManager.addOnBackStackChangedListener(this);
        CustomerFragType customerFragType = (CustomerFragType) getIntent().getExtras().getSerializable(BUNDLE_KEY_CUSTOMER_FRAG_TYPE);
        assert customerFragType != null;
        switch (customerFragType) {
            case TYPE_DASHBOARD:
                FragmentHelper.replaceFragment(R.id.container, this, DashboardFragment.newInstance(), DashboardFragment.class.getSimpleName(), true, true);
                break;

            case TYPE_ACCOUNT_INFO:
                FragmentHelper.replaceFragment(R.id.container, this, AccountInfoFragment.newInstance(), AccountInfoFragment.class.getSimpleName(), true, true);
                break;

            case TYPE_ADDRESS_BOOK:
                FragmentHelper.replaceFragment(R.id.container, this, AddressBookFragment.newInstance(), AddressBookFragment.class.getSimpleName(), true, true);
                break;

            case TYPE_ORDER_LIST:
                FragmentHelper.replaceFragment(R.id.container, this, OrderListFragment.newInstance(), OrderListFragment.class.getSimpleName(), true, true);
                break;
            case TYPE_WISHLIST:
                if (AppSharedPref.isAllowedWishlist(this)) {
                    FragmentHelper.replaceFragment(R.id.container, this, WishlistFragment.newInstance(), WishlistFragment.class.getSimpleName(), true, true);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_search) {
            mBinding.searchView.openSearch();
        } else if (item.getItemId() == R.id.menu_item_bag) {
            IntentHelper.goToBag(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        if (mSupportFragmentManager.getBackStackEntryCount() == 0) {
            return;
        }

        String tag = mSupportFragmentManager.getBackStackEntryAt(mSupportFragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment fragment = mSupportFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            return;
        }
        if (fragment instanceof DashboardFragment) {
            setTitle(getString(R.string.dashboard));
        } else if (fragment instanceof OrderListFragment) {
            setTitle(getString(R.string.all_orders));
        } else if (fragment instanceof WishlistFragment) {
            setTitle(getString(R.string.wishlist));
        } else if (fragment instanceof OrderFragment) {
            setTitle(getString(R.string.my_order));
        } else if (fragment instanceof AddressBookFragment) {
            setTitle(getString(R.string.address_book));
        } else //noinspection StatementWithEmptyBody
            if (fragment instanceof NewAddressFragment) {
                // do nothing
            } else if (fragment instanceof AccountInfoFragment) {
                setTitle(getString(R.string.account_info));
            }
    }


    @Override
    public void onBackPressed() {

        if (mBinding.searchView.isOpen()) {
            mBinding.searchView.closeSearch();
            return;
        }
        /*IN CASE OF LAST FRAGMENT ADDED TO BACKSTACK*/
        if (mSupportFragmentManager.getBackStackEntryCount() == 1) {
            finish();
        }

        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MaterialSearchView.RC_MATERIAL_SEARCH_VOICE:
                switch (resultCode) {
                    case RESULT_OK:
                        ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && matches.size() > 0) {
                            String searchWrd = matches.get(0);
                            if (!TextUtils.isEmpty(searchWrd)) {
                                mBinding.searchView.setQuery(searchWrd, false);
                            }
                        }
                        break;
                }
        }
    }

    @Override
    public String getScreenTitle() {
        return TAG;
    }

}
