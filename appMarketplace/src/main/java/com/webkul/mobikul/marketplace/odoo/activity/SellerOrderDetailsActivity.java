package com.webkul.mobikul.marketplace.odoo.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerOrderDetailsResponse;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.constant.BundleConstant;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aastha.gupta on 1/3/18 in Mobikul_Odoo_Application.
 */

public class SellerOrderDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callConnection();
    }

    public void callConnection() {
        MarketplaceApiConnection.getSellerOrderDetails(this,
                String.valueOf(getIntent().getExtras().getInt(BundleConstant.BUNDLE_KEY_ORDER_NAME))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SellerOrderDetailsResponse>(this) {

            @Override
            public void onNext(@NonNull SellerOrderDetailsResponse response) {
                super.onNext(response);
//                mBinding.getData().setLazyLoading(false);
//
//                if (mIsFirstCall) {
////                mIsFirstCall = false;
//                    mBinding.setData(response);
////            mBinding.setHandler(new CatalogProductActivityHandler(this));
//                    mBinding.executePendingBindings();
//
//            /*if there at least 4 prouduct present then only show the item decoration */
////                if (mBinding.getData().getTotalCount() >= 4) {
////                    mBinding.productCatalogRv.addItemDecoration(new DividerItemDecoration(CatalogProductActivity.this, LinearLayout.HORIZONTAL));
////                    mBinding.productCatalogRv.addItemDecoration(new DividerItemDecoration(CatalogProductActivity.this, LinearLayout.VERTICAL));
////                }
////                mBinding.productCatalogRv.addItemDecoration(new HeaderDecoration(CatalogProductActivity.this, mBinding.productCatalogRv, R.layout.item_featured_category));
//
//                /*BETTER REPLACE SOME CONTAINER INSTEAD OF WHOLE PAGE android.R.id.content */
//                    if (mBinding.getData().getSellerOrderLines().isEmpty()) {
//                        FragmentHelper.replaceFragment(R.id.container, SellerOrdersListActivity.this, EmptyFragment.newInstance(R.drawable.ic_vector_empty_order, getString(R.string.no_order_placed),
//                                "", true,
//                                EmptyFragment.EmptyFragType.TYPE_ORDER.ordinal()), EmptyFragment.class.getSimpleName(), false, false);
//                    } else {
//                /*Remove empty fragment if added*/
//                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(EmptyFragment.class.getSimpleName());
//                        if (fragment != null) {
//                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                        }
//                        initProductCatalogRv();
//                    }
//                } else {
//                /*update offset from new response*/
//                    mBinding.getData().setOffset(response.getOffset());
////                    mBinding.getData().setLimit(response.getLimit());
//                    mBinding.getData().getSellerOrderLines().addAll(response.getSellerOrderLines());
//                    mBinding.orderListRv.getAdapter().notifyDataSetChanged();
//
//                }
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
}
