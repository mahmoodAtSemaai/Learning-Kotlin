package com.webkul.mobikul.marketplace.odoo.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.databinding.ActivitySellerDashboardBinding;
import com.webkul.mobikul.marketplace.odoo.handler.SellerDashboardActivityHandler;
import com.webkul.mobikul.marketplace.odoo.model.SellerDashboardResponse;
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
 * Created by aastha.gupta on 15/2/18 in Mobikul_Odoo_Application.
 */

public class SellerDashBoardActivity extends BaseActivity {
    ActivitySellerDashboardBinding mBinding;
    private static final String TAG = "SellerDashboardActivity";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_search) {
            mBinding.searchView.openSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mBinding.searchView.isOpen()) {
            mBinding.searchView.closeSearch();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_dashboard);
        setSupportActionBar(mBinding.toolbar);
        mBinding.setTitle(getString(R.string.seller_dashboard));
        MarketplaceApiConnection.getSellerDashboardData(this).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SellerDashboardResponse>(this) {

            @Override
            public void onNext(@NonNull SellerDashboardResponse response) {
                super.onNext(response);
                if (response.isAccessDenied()){
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(SellerDashBoardActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(SellerDashBoardActivity.this);
                            Intent i = new Intent(SellerDashBoardActivity.this, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SellerDashBoardActivity.this.getClass().getSimpleName());
                            startActivity(i);
                        }
                    });
                }else {
                    mBinding.setData(response.getSellerDashboard());
                    mBinding.setHandler(new SellerDashboardActivityHandler(SellerDashBoardActivity.this));
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
}
