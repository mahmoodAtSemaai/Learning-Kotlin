package com.webkul.mobikul.odoo.activity;

import android.app.Activity;
import android.content.Intent;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.google.android.material.snackbar.Snackbar;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.cart.BagItemsRecyclerAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.constant.ApplicationConstant;
import com.webkul.mobikul.odoo.databinding.ActivityBagBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.fragment.EmptyFragment;
import com.webkul.mobikul.odoo.handler.bag.BagActivityHandler;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.ApiRequestHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CustomerHelper;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.ReferralResponse;
import com.webkul.mobikul.odoo.helper.IntentHelper;
import com.webkul.mobikul.odoo.model.cart.BagResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CUSTOMER_FRAG_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_ORDER_ID;


public class BagActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener {
    @SuppressWarnings("unused")
    private static final String TAG = "BagActivity";
    private ActivityBagBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bag);
        setSupportActionBar(binding.toolbar);
        showBackButton(true);
        mSupportFragmentManager.addOnBackStackChangedListener(this);
        AppSharedPref.setIsCustomerWantToRedeemPoints(this, false);
        hitApiForLoyaltyPoints(AppSharedPref.getCustomerId(this));
        onCheckedChangeRedeemPoints();
    }

    @Override
    public String getScreenTitle() {
        return TAG;
    }


    @Override
    protected void onResume() {
        super.onResume();
        binding.setData(null);
        getCartData();
    }

    public void getCartData() {
        ApiConnection.getCartData(this, AppSharedPref.getIsCustomerWantToRedeemPoints(this)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BagResponse>(this) {

            @Override
            public void onNext(@NonNull BagResponse bagResponse) {
                super.onNext(bagResponse);
                if (bagResponse.isAccessDenied()) {
                    IntentHelper.redirectToSignUpActivity(BagActivity.this);
                } else {
                    handleResponseData(bagResponse);
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


    private void handleResponseData(BagResponse bagResponse) {
        binding.setData(bagResponse);
        binding.setHandler(new BagActivityHandler(this, bagResponse));
        if (bagResponse.getItems().isEmpty()) {
            showEmptyFragment();
        } else {
            binding.rvProducts.setAdapter(new BagItemsRecyclerAdapter(this, bagResponse.getItems()));
            setButtonClickListeners(bagResponse);
        }
        AppSharedPref.setOrderId(this, bagResponse.getOrderId());
    }

    private void setButtonClickListeners(BagResponse bagResponse) {
        binding.btnGoToCheckout.setOnClickListener(view -> {
            FirebaseAnalyticsImpl.logBeginCheckoutEvent(this);
            startActivity(new Intent(this, CheckoutActivity.class).putExtra(BUNDLE_KEY_ORDER_ID, bagResponse.getOrderId()));
        });
    }

    private void showEmptyFragment() {
        FragmentHelper.replaceFragment(R.id.container, this, EmptyFragment.newInstance(R.drawable.ic_vector_empty_bag, getString(R.string.empty_bag)
                , getString(R.string.add_item_to_your_bag_now), false, EmptyFragment.EmptyFragType.TYPE_CART.ordinal()), EmptyFragment.class.getSimpleName(), false, false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bag_menu, menu);
        MenuItem wishlistMenuItem = menu.findItem(R.id.menu_item_wishlist);
        wishlistMenuItem.setVisible(true);
        wishlistMenuItem.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(this, CustomerBaseActivity.class);
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

    public void onCheckedChangeRedeemPoints(){
        binding.cbUsePoints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppSharedPref.setIsCustomerWantToRedeemPoints(BagActivity.this, isChecked);
                getCartData();
            }
        });
    }


    public void hitApiForLoyaltyPoints(String userId){
        ApiConnection.getLoyaltyPoints(this, userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<ReferralResponse>(this) {

            @Override
            public void onNext(@NonNull ReferralResponse response) {
                super.onNext(response);
                if (response.getStatus() == ApplicationConstant.SUCCESS) {
                    showPoints(response.getRedeemHistory());
                }
                else {
                    SnackbarHelper.getSnackbar((Activity) BagActivity.this, response.getMessage(), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                }
            }
        });
    }

    public void showPoints(Integer loyaltyPoints){
        binding.tvSemaaiPoints.setText(loyaltyPoints.toString());
        if (loyaltyPoints < 1) {
            binding.cbUsePoints.setChecked(false);
            binding.cbUsePoints.setEnabled(false);
        }
    }

}
