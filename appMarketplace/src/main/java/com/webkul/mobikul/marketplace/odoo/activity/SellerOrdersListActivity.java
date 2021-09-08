package com.webkul.mobikul.marketplace.odoo.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.widget.Toast;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.adapter.SellerOrdersListRvAdapter;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.databinding.ActivitySellerOrderListBinding;
import com.webkul.mobikul.marketplace.odoo.model.request.SellerOrdersListRequest;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerOrdersListResponse;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.fragment.EmptyFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.EndlessRecyclerViewScrollListener;
import com.webkul.mobikul.odoo.helper.FragmentHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.marketplace.odoo.constant.MarketplaceBundleConstant.BUNDLE_KEY_ORDER_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

/**
 * Created by aastha.gupta on 22/2/18 in Mobikul_Odoo_Application.
 * An activity to show Order list for a particular seller using seller Login token
 */

public class SellerOrdersListActivity extends BaseActivity {
    ActivitySellerOrderListBinding mBinding;
    private boolean mIsFirstCall = true;
    //    private Toast mToast;
    private CustomToast mToast;
    private String mState = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_search).setVisible(false);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_order_list);
        setSupportActionBar(mBinding.toolbar);
        showBackButton(true);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(BUNDLE_KEY_ORDER_TYPE)) {
            mState = getIntent().getExtras().getString(BUNDLE_KEY_ORDER_TYPE);
        }
        mBinding.orderListRv.setLayoutManager(new LinearLayoutManager(this)); /*default initialization*/
        requestDataFromApi();
    }


    private void requestDataFromApi() {
        init();
        mBinding.setTitle(getString(R.string.seller_orders));
        callApi();
    }

    protected void callApi() {
        int offset = 0;
        if (!mIsFirstCall) {
            offset = mBinding.getData().getOffset() + AppSharedPref.getItemsPerPage(this);
        }

        MarketplaceApiConnection.getSellerOrdersList(this, new SellerOrdersListRequest(mState, offset, AppSharedPref.getItemsPerPage(this))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SellerOrdersListResponse>(this) {

            @Override
            public void onNext(@NonNull SellerOrdersListResponse response) {
                super.onNext(response);

                if (response.isAccessDenied()){
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(SellerOrdersListActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(SellerOrdersListActivity.this);
                            Intent i = new Intent(SellerOrdersListActivity.this, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, SellerOrdersListActivity.this.getClass().getSimpleName());
                            startActivity(i);
                        }
                    });

                }else {
                    mBinding.getData().setLazyLoading(false);

                    if (mIsFirstCall) {
                        mBinding.setData(response);
                        mBinding.executePendingBindings();


                        if (mBinding.getData().getSellerOrderLines().isEmpty()) {
                            FragmentHelper.replaceFragment(R.id.container, SellerOrdersListActivity.this, EmptyFragment.newInstance(R.drawable.ic_vector_empty_order, getString(R.string.no_order_placed),
                                    "", true,
                                    EmptyFragment.EmptyFragType.TYPE_ORDER.ordinal()), EmptyFragment.class.getSimpleName(), false, false);
                        } else {
                            /*Remove empty fragment if added*/
                            Fragment fragment = getSupportFragmentManager().findFragmentByTag(EmptyFragment.class.getSimpleName());
                            if (fragment != null) {
                                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                            }
                            initProductCatalogRv();
                        }
                    } else {
                        /*update offset from new response*/
                        mBinding.getData().setOffset(response.getOffset());
                        mBinding.getData().getSellerOrderLines().addAll(response.getSellerOrderLines());
                        mBinding.orderListRv.getAdapter().notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                   /* this might not be true for Rx Callbacks..*/
                // onFailure can be called if the activity is sudden closed, thus dispatcher calls the cancel which result in calling of onFailure..
                if (mBinding.getData() != null) {
                    mBinding.getData().setLazyLoading(false);
                }
                super.onError(t);
            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * Initialize the activity to act like a new request coming...
     */
    private void init() {
          /*Init Data */
        mIsFirstCall = true;
        mBinding.setData(new SellerOrdersListResponse());
        mBinding.getData().setLazyLoading(true);
    }

    private void showPositionToast(int lastVisibleItemPosition) {
        String toastString = lastVisibleItemPosition + 1 + " " + getString(R.string.of_toast_for_no_of_item) + " " + mBinding.getData().getTcount();
        if (mToast != null) {
            mToast.setToastMsg(toastString);
            mToast.show();
        } else {
            mToast = CustomToast.makeText(this, toastString, Toast.LENGTH_SHORT, R.style.GenericStyleableToast);
            mToast.show();
        }
    }

    private void initProductCatalogRv() {
        mBinding.orderListRv.setAdapter(new SellerOrdersListRvAdapter(this, mBinding.getData().getSellerOrderLines()));
        mBinding.orderListRv.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mBinding.orderListRv.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (AppSharedPref.getItemsPerPage(SellerOrdersListActivity.this) <= totalItemsCount) {
                    mIsFirstCall = false;
                    mBinding.getData().setLazyLoading(true);
                    callApi();
                }
            }
        });

        mBinding.orderListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    showPositionToast(lastVisibleItemPosition);
                }
            }
        });

    }
}
