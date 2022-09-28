package com.webkul.mobikul.odoo.activity;

import android.app.SearchManager;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.speech.RecognizerIntent;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.catalog.CatalogProductListAdapter;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.constant.BundleConstant;
import com.webkul.mobikul.odoo.core.utils.AppConstantsKt;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.custom.MaterialSearchView;
import com.webkul.mobikul.odoo.data.entity.ProductListEntity;
import com.webkul.mobikul.odoo.data.response.ProductListResponse;
import com.webkul.mobikul.odoo.databinding.ActivityProductCatalogBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.fragment.EmptyFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.helper.EndlessRecyclerViewScrollListener;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.ViewHelper;
import com.webkul.mobikul.odoo.core.data.response.BaseResponseNew;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_SEARCH_DOMAIN;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_SELLER_ID;

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

public class CatalogProductActivity extends BaseActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "CatalogProductActivity";
    public ActivityProductCatalogBinding mBinding;
    private boolean mIsFirstCall = true;
    //    private Toast mToast;
    private CustomToast mToast;
    public static final int VIEW_TYPE_LIST = 1;
    public static final int VIEW_TYPE_GRID = 2;

    private CustomObserver<BaseResponseNew<ProductListResponse>> mProductListEntityObserver = new CustomObserver<BaseResponseNew<ProductListResponse>>(this) {
        @Override
        public void onSubscribe(Disposable d) {
            super.onSubscribe(d);
        }

        @Override
        public void onNext(BaseResponseNew<ProductListResponse> productListResponse) {
            super.onNext(productListResponse);
            if (productListResponse.getStatusCode() == AppConstantsKt.HTTP_ERROR_UNAUTHORIZED_REQUEST) {
                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(CatalogProductActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        AppSharedPref.clearCustomerData(CatalogProductActivity.this);
                        Intent i = new Intent(CatalogProductActivity.this, SignInSignUpActivity.class);
                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, CatalogProductActivity.class.getSimpleName());
                        startActivity(i);
                    }
                });
            }
            Gson gson = new Gson();
            ProductListEntity productListEntity = gson.fromJson(gson.toJson(productListResponse.getData()), ProductListEntity.class);

            mBinding.getData().setLazyLoading(false);

            if (mIsFirstCall) {
                mBinding.setData(productListEntity);
                mBinding.executePendingBindings();

                if (mBinding.getData().getProducts().isEmpty()) {
                    FragmentHelper.replaceFragment(R.id.container, CatalogProductActivity.this, EmptyFragment.newInstance(R.drawable.ic_vector_empty_product_catalog, getString(R.string.empty_product_catalog), getString(R.string.try_different_category_or_search_keyword_maybe), true,
                            EmptyFragment.EmptyFragType.TYPE_CATALOG_PRODUCT.ordinal()), EmptyFragment.class.getSimpleName(), false, false);
                    mBinding.floatingButton.setVisibility(View.GONE);
                } else {
                    /*Remove empty fragment if added*/
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(EmptyFragment.class.getSimpleName());
                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    initProductCatalogRv();
                    mBinding.floatingButton.setVisibility(View.VISIBLE);
                }
            } else {
                mBinding.getData().setOffset(productListEntity.getOffset());
                mBinding.getData().setTCount(productListEntity.getTCount());
                mBinding.getData().getProducts().addAll(productListEntity.getProducts());
                mBinding.productCatalogRv.getAdapter().notifyDataSetChanged();
                mBinding.floatingButton.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onError(Throwable t) {
            super.onError(t);
        }

        @Override
        public void onComplete() {
            super.onComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_catalog);
        setSupportActionBar(mBinding.toolbar);
        showBackButton(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //catching intents in singleTop activities
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        init();
        requestDataFromApi();
    }

    private void requestDataFromApi() {
        mBinding.setTitle(getIntent().getExtras().getString(BUNDLE_KEY_CATEGORY_NAME));
        callApi();
    }

    protected void callApi() {
        int offset = 0;
        if (!mIsFirstCall) {
            offset = mBinding.getData().getOffset() + AppSharedPref.getItemsPerPage(this);
        }
        CatalogHelper.CatalogProductRequestType catalogProductRequestType = (CatalogHelper.CatalogProductRequestType) getIntent().getSerializableExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE);
        Observable<BaseResponseNew<ProductListResponse>> productListResponseObservable = null;
        switch (catalogProductRequestType) {
            case SELLER_PRODUCTS:
                productListResponseObservable = ApiConnection.getSellerProducts(this, Integer.parseInt(getIntent().getExtras().getString(BUNDLE_KEY_SELLER_ID)), offset, AppSharedPref.getItemsPerPage(this));
                break;

            case FEATURED_CATEGORY:
            case GENERAL_CATEGORY:
            case BANNER_CATEGORY:
                productListResponseObservable = ApiConnection.getCategoryProductList(this, getIntent().getExtras().getInt(BUNDLE_KEY_CATEGORY_ID), offset, AppSharedPref.getItemsPerPage(this));
                break;

            case SEARCH_QUERY:
                productListResponseObservable = ApiConnection.getProductSearchResponse(this, true, getIntent().getExtras().getString(SearchManager.QUERY), offset, AppSharedPref.getItemsPerPage(this));
                break;

            case SEARCH_DOMAIN:
                productListResponseObservable = ApiConnection.getProductSearchResponse(this, true, getIntent().getExtras().getString(BUNDLE_KEY_SEARCH_DOMAIN), offset, AppSharedPref.getItemsPerPage(this));
                break;
        }

        productListResponseObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mProductListEntityObserver);
    }


    /**
     * Search Widget fire ACTION_SEARCH intent twice when user search a word
     * http://stackoverflow.com/a/19392225
     */

//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//        init();
//        requestDataFromApi();
//    }

    /**
     * Initialize the activity to act like a new request coming...
     */
    private void init() {
        /*Init Data */
        mIsFirstCall = true;
        mBinding.setData(new ProductListEntity());
        mBinding.getData().setLazyLoading(true);
        AppSharedPref.setItemsPerPage(this, BuildConfig.DEFAULT_ITEM_PER_PAGE);
    }

    private void showPositionToast(int lastVisibleItemPosition) {
        String toastString = lastVisibleItemPosition + " " + getString(R.string.of_toast_for_no_of_item) + " " + mBinding.getData().getTCount();
        if (mToast != null) {
            mToast.setToastMsg(toastString);
            mToast.show();
        } else {
            mToast = CustomToast.makeText(CatalogProductActivity.this, toastString, Toast.LENGTH_SHORT, R.style.GenericStyleableToast);
            mToast.show();
        }
    }

    private void initProductCatalogRv() {
        mBinding.productCatalogRv.setAdapter(new CatalogProductListAdapter(this, mBinding.getData().getProducts(), VIEW_TYPE_LIST));

        if (AppSharedPref.isGridview(this)) {
            int spanCount = ViewHelper.getSpanCount(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == mBinding.getData().getProducts().size()) {
                        return spanCount;
                    }
                    return 1;
                }
            });
            mBinding.productCatalogRv.setLayoutManager(gridLayoutManager);
            mBinding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_list));

            mBinding.productCatalogRv.addOnScrollListener(new EndlessRecyclerViewScrollListener((GridLayoutManager) mBinding.productCatalogRv.getLayoutManager()) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (AppSharedPref.getItemsPerPage(CatalogProductActivity.this) <= totalItemsCount) {
                        mIsFirstCall = false;
                        mBinding.getData().setLazyLoading(true);
                        callApi();
                    }
                }
            });

        } else {
            mBinding.productCatalogRv.setLayoutManager(new LinearLayoutManager(this));
            mBinding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_grid));

            mBinding.productCatalogRv.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mBinding.productCatalogRv.getLayoutManager()) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (AppSharedPref.getItemsPerPage(CatalogProductActivity.this) <= totalItemsCount) {
                        mIsFirstCall = false;
                        mBinding.getData().setLazyLoading(true);
                        callApi();
                    }
                }
            });
        }

        mBinding.productCatalogRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    int lastVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                    if (mBinding.getData().getProducts().size() <= 9) {
                        if ((lastVisibleItemPosition + 1) == mBinding.getData().getTCount()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition + 1);
                    }
                    if (mBinding.getData().getProducts().size() > 9) {

                        if ((lastVisibleItemPosition) == mBinding.getData().getTCount()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition);
                    }


                } else {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (mBinding.getData().getProducts().size() <= 5) {
                        if ((lastVisibleItemPosition + 1) == mBinding.getData().getTCount()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition + 1);
                    }
                    if (mBinding.getData().getProducts().size() > 5) {
                        if ((lastVisibleItemPosition) == mBinding.getData().getTCount()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition);
                    }


                }
            }
        });

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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_search) {
            mBinding.searchView.openSearch();
        }
        return super.onOptionsItemSelected(item);
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
                break;
            case MaterialSearchView.RC_CAMERA_SEARCH:
                switch (resultCode) {
                    case RESULT_OK:
                        mBinding.searchView.setQuery(data.getStringExtra(BundleConstant.CAMERA_SEARCH_HELPER), true);
                        break;
                }
                break;
        }
    }


    public void onClickViewSwitcher(View view) {
        if (AppSharedPref.isGridview(this)) {
            mBinding.productCatalogRv.setLayoutManager(new LinearLayoutManager(this));
            mBinding.productCatalogRv.setAdapter(new CatalogProductListAdapter(this, mBinding.getData().getProducts(), VIEW_TYPE_LIST));
            AppSharedPref.setGridview(this, false);
            mBinding.footerTv.setVisibility(View.GONE);
            mBinding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_grid));
        } else {
            int spanCount = ViewHelper.getSpanCount(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == mBinding.getData().getProducts().size()) {
                        return spanCount;
                    }
                    return 1;
                }
            });
            mBinding.productCatalogRv.setLayoutManager(gridLayoutManager);
            mBinding.productCatalogRv.setAdapter(new CatalogProductListAdapter(this, mBinding.getData().getProducts(), VIEW_TYPE_LIST));
            AppSharedPref.setGridview(this, true);
            mBinding.footerTv.setVisibility(View.GONE);
            mBinding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_list));
        }
    }

    public void onClickBackToTop(View view) {
        if (mBinding.productCatalogRv.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) mBinding.productCatalogRv.getLayoutManager();
            gridLayoutManager.scrollToPositionWithOffset(0, 0);
            mBinding.footerTv.setVisibility(View.GONE);
        } else {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mBinding.productCatalogRv.getLayoutManager();
            linearLayoutManager.scrollToPositionWithOffset(0, 0);
            mBinding.footerTv.setVisibility(View.GONE);
        }
    }

    @Override
    public String getScreenTitle() {
        return TAG;
    }

}