package com.webkul.mobikul.marketplace.odoo.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.adapter.SellerReviewAdapter;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.databinding.ActivitySellerProfileBinding;
import com.webkul.mobikul.marketplace.odoo.handler.SellerProfileActivityHandler;
import com.webkul.mobikul.marketplace.odoo.handler.SellerReviewHandler;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerProfilePageResponse;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.adapter.home.ProductDefaultStyleAdapter;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.generic.ProductData;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.marketplace.odoo.constant.MarketplaceBundleConstant.BUNDLE_KEY_SELLER_ID;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.SLIDER_MODE_DEFAULT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

/**
 * Created by aastha.gupta on 19/9/17.
 */

public class SellerProfileActivity extends BaseActivity {
    private final int REQUEST_CODE_LOGIN = 10001;
    ActivitySellerProfileBinding mBinding;
    private static final String TAG = "SellerProfileActivity";

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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_profile);
        setSupportActionBar(mBinding.toolbar);
        MarketplaceApiConnection.getSellerProfileData(this, getIntent().getExtras()
                .getString(BUNDLE_KEY_SELLER_ID)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SellerProfilePageResponse>(this) {

            @Override
            public void onNext(@NonNull SellerProfilePageResponse response) {
                super.onNext(response);
                if (response.isAccessDenied()){
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(SellerProfileActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(SellerProfileActivity.this);
                            Intent i = new Intent(SellerProfileActivity.this, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SellerProfileActivity.this.getClass().getSimpleName());
                            startActivity(i);
                        }
                    });
                }else {


                    if (response.isSuccess()) {
                        mBinding.setData(response.getSellerInfo());
                        mBinding.setHandler(new SellerProfileActivityHandler(SellerProfileActivity.this, mBinding));
                        mBinding.setReviewHandler(new SellerReviewHandler(SellerProfileActivity.this, response.getSellerInfo().getSellerId()));
                        mBinding.setTitle(response.getSellerInfo().getName());
                        mBinding.executePendingBindings();
                        mBinding.sellerRatingRecyclerView.setAdapter(new SellerReviewAdapter(SellerProfileActivity.this, response.getSellerInfo().getSellerReviews()));
                        mBinding.sellerRatingRecyclerView.setNestedScrollingEnabled(false);
                        mBinding.sellerRecentCollectionRv.setAdapter(new ProductDefaultStyleAdapter(SellerProfileActivity.this, (ArrayList<ProductData>) response.getSellerInfo().getSellerProducts().getProducts(), SLIDER_MODE_DEFAULT));
                    }
                    /*Loading Banner Data*/
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == RESULT_OK) {
                recreate();
            }
        }
    }

    @Override
    public String getScreenTitle() {
        return TAG;
    }
}
