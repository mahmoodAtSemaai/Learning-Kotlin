package com.webkul.mobikul.marketplace.odoo.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.adapter.MarketplaceLandingPageBestSellerListRvAdapter;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.databinding.ActivityMarketplaceLandingBinding;
import com.webkul.mobikul.marketplace.odoo.handler.MarketplaceLandingActivityHandler;
import com.webkul.mobikul.marketplace.odoo.model.MarketplaceLandingPageData;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

/**
 * Created by aastha.gupta on 2/11/17 in Mobikul_Odoo_Application.
 */

public class MarketplaceLandingActivity extends BaseActivity {
    private ActivityMarketplaceLandingBinding mBinding;
    private static final String TAG = "MarketPlaceLandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_marketplace_landing);
        setSupportActionBar(mBinding.toolbar);
        MarketplaceApiConnection.getLandingPageData(this).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<MarketplaceLandingPageData>(this) {

            @Override
            public void onNext(@NonNull MarketplaceLandingPageData response) {
                super.onNext(response);
                if (response.isAccessDenied()) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(MarketplaceLandingActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(MarketplaceLandingActivity.this);
                            Intent i = new Intent(MarketplaceLandingActivity.this, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, MarketplaceLandingActivity.this.getClass().getSimpleName());
                            startActivity(i);
                        }
                    });
                } else {

                    if (AppSharedPref.isLoggedIn(MarketplaceLandingActivity.this)) {
                        if (AppSharedPref.isSeller(MarketplaceLandingActivity.this)) {
                            mBinding.setShowSellerLoginButton(false);
                        } else {
                            mBinding.setShowSellerLoginButton(true);
                        }
                    } else {
                        mBinding.setShowSellerLoginButton(true);
                    }
                    mBinding.setData(response);
                    mBinding.setTitle(getString(R.string.marketplace));
                    mBinding.setHandler(new MarketplaceLandingActivityHandler(MarketplaceLandingActivity.this));
                    mBinding.bestSellerListRv.setAdapter(new MarketplaceLandingPageBestSellerListRvAdapter(MarketplaceLandingActivity.this
                            , response.getSellersDetail()));
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
    public String getScreenTitle() {
        return TAG;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
