package com.webkul.mobikul.odoo.activity;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.cart.BagItemsRecyclerAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.ActivityBagBinding;
import com.webkul.mobikul.odoo.fragment.EmptyFragment;
import com.webkul.mobikul.odoo.handler.bag.BagActivityHandler;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CustomerHelper;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.model.cart.BagResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
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

public class BagActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener {
    @SuppressWarnings("unused")
    private static final String TAG = "BagActivity";
    private ActivityBagBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bag);
        setSupportActionBar(mBinding.toolbar);
        showBackButton(true);
        mSupportFragmentManager.addOnBackStackChangedListener(this);
    }

    @Override
    public String getScreenTitle() {
        return TAG;
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCartData();
    }

    public void getCartData() {
        ApiConnection.getCartData(this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BagResponse>(this) {

            @Override
            public void onNext(@NonNull BagResponse bagResponse) {
                super.onNext(bagResponse);
                if (bagResponse.isAccessDenied()) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(BagActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(BagActivity.this);
                            Intent i = new Intent(BagActivity.this, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, BagActivity.class.getSimpleName());
                            startActivity(i);
                        }
                    });
                } else {
                    mBinding.setData(bagResponse);
                    mBinding.setHandler(new BagActivityHandler(BagActivity.this, bagResponse));

                    if (bagResponse.getItems().isEmpty()) {

                        FragmentHelper.replaceFragment(R.id.container, BagActivity.this, EmptyFragment.newInstance(R.drawable.ic_vector_empty_bag, getString(R.string.empty_bag)
                                , getString(R.string.add_item_to_your_bag_now), false, EmptyFragment.EmptyFragType.TYPE_CART.ordinal()), EmptyFragment.class.getSimpleName(), false, false);
                    } else {

                        mBinding.productsRv.setAdapter(new BagItemsRecyclerAdapter(BagActivity.this, bagResponse.getItems()));
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bag_menu, menu);
        MenuItem wishlistMenuItem = menu.findItem(R.id.menu_item_wishlist);
        if (AppSharedPref.isAllowedWishlist(this) && AppSharedPref.isLoggedIn(this)) {
            wishlistMenuItem.setVisible(true);
        } else {
            wishlistMenuItem.setVisible(false);
        }
        wishlistMenuItem.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(BagActivity.this, CustomerBaseActivity.class);
            intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_WISHLIST);
            startActivity(intent);
            return true;
        });
        return true;
    }

    @Override
    protected void onPause() {

        super.onPause();

        EmptyFragment emptyFragment = (EmptyFragment) mSupportFragmentManager.findFragmentByTag(EmptyFragment.class.getSimpleName());
        if (emptyFragment != null) {
            boolean myFragXwasVisible = emptyFragment.isVisible();

            // If it was your particular Fragment that was visible...
            if (myFragXwasVisible) {
                FragmentTransaction trans = mSupportFragmentManager.beginTransaction();
                trans.remove(emptyFragment).commit();
            }
        }


    }

    @Override
    public void onBackStackChanged() {
        if (mSupportFragmentManager.getBackStackEntryCount() == 0) {
            setTitle(getString(R.string.bag));
        }
    }
}
