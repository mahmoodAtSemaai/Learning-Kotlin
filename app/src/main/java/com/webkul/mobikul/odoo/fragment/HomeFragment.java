package com.webkul.mobikul.odoo.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.adapter.home.FeaturedCategoriesRvAdapter;
import com.webkul.mobikul.odoo.adapter.home.HomeBannerAdapter;
import com.webkul.mobikul.odoo.adapter.home.NavDrawerCategoryStartRvAdapter;
import com.webkul.mobikul.odoo.adapter.home.ProductDefaultStyleRvAdapter;
import com.webkul.mobikul.odoo.adapter.product.AlternativeProductsRvAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.connection.CustomRetrofitCallback;
import com.webkul.mobikul.odoo.database.SaveData;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.FragmentHomeBinding;
import com.webkul.mobikul.odoo.databinding.ItemProductSliderDefaultStyleBinding;
import com.webkul.mobikul.odoo.databinding.ItemProductSliderFixedStyleBinding;
import com.webkul.mobikul.odoo.handler.generic.ProductSliderHandler;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.ViewHelper;
import com.webkul.mobikul.odoo.model.generic.ProductData;
import com.webkul.mobikul.odoo.model.generic.ProductSliderData;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.SLIDER_MODE_DEFAULT;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.SLIDER_MODE_FIXED;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class HomeFragment extends BaseFragment implements CustomRetrofitCallback {

    @SuppressWarnings("unused")
    private static final String TAG = "HomeFragment";
    public FragmentHomeBinding mBinding;

    public static HomeFragment newInstance(@Nullable HomePageResponse homePageResponse) {
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.hideKeyboard(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecentProductList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HomePageResponse homePageResponse = getArguments().getParcelable(BUNDLE_KEY_HOME_PAGE_RESPONSE);
        if (homePageResponse == null) {
            hitApiForFetchingData();
            return;
        }
        new SaveData(getActivity(), homePageResponse);
        loadHomePage(homePageResponse, false);
    }

    private void hitApiForFetchingData() {
        ApiConnection.getHomePageData(getContext()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<HomePageResponse>(getContext()) {

            @Override
            public void onNext(@NonNull HomePageResponse homePageResponse) {
                super.onNext(homePageResponse);
                new SaveData(getActivity(), homePageResponse);

                Log.i("HIT DATA === ", homePageResponse.toString());
                loadHomePage(homePageResponse, true);
            }

            @Override
            public void onComplete() {
                mBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    @Override
    public void onSuccess(Object object) {
//        loadHomePage((HomePageResponse) object);
    }

    @Override
    public void onError(Throwable t) {

    }

//    private void hitApiForFetchingData() {
//        ApiConnection.getHomePageDataResponse(getContext(), this);
//
//    }

    private void loadHomePage(HomePageResponse homePageResponse, boolean isFromApi) {
        if (isFromApi){
            homePageResponse.updateSharedPref(getContext(), "");
        }
        mBinding.setData(homePageResponse);
        mBinding.swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        hitApiForFetchingData();
                    }
                }
        );
        /*LEFT CATEGORIES DRAWER*/
        ((HomeActivity) getActivity()).mBinding.categoryRv.setAdapter(new NavDrawerCategoryStartRvAdapter(getContext(), homePageResponse.getCategories()));
        if (homePageResponse.getLanguageMap().size() > 0) {
            ((HomeActivity) getActivity()).mBinding.setLanguageData(homePageResponse.getLanguageMap());
        } else {
            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(getContext());
            if (sqlLiteDbHelper.getHomeScreenData() != null) {
                if (sqlLiteDbHelper.getHomeScreenData().getLanguageMap() != null) {
                    ((HomeActivity) getActivity()).mBinding.setLanguageData(sqlLiteDbHelper.getHomeScreenData().getLanguageMap());
                }
            }
        }


        /*FEATURED CATEGORIES*/
        mBinding.featuredCategoriesRv.setAdapter(new FeaturedCategoriesRvAdapter(getContext(), homePageResponse.getFeaturedCategories()));

        /*BANNER SLIDERS*/
        mBinding.bannerViewPager.setAdapter(new HomeBannerAdapter(getContext(), homePageResponse.getBannerImages()));
        mBinding.bannerDotsTabLayout.setupWithViewPager(mBinding.bannerViewPager, true);

        /*PRODUCT SLIDES...*/
        mBinding.productSliderContainer.removeAllViews();
        for (ProductSliderData productSliderData : homePageResponse.getProductSliders()) {
            switch (productSliderData.getSliderMode()) {
                case SLIDER_MODE_DEFAULT:
                    ItemProductSliderDefaultStyleBinding itemProductSliderDefaultStyleBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_product_slider_default_style, mBinding.productSliderContainer, true);
                    itemProductSliderDefaultStyleBinding.setData(productSliderData);
                    itemProductSliderDefaultStyleBinding.setHandler(new ProductSliderHandler(getContext()));
                    itemProductSliderDefaultStyleBinding.productsRv.setAdapter(new ProductDefaultStyleRvAdapter(getContext(), (ArrayList<ProductData>) productSliderData.getProducts(), SLIDER_MODE_DEFAULT));
                    break;

                case SLIDER_MODE_FIXED:
                    ItemProductSliderFixedStyleBinding itemProductSliderFixedStyleBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_product_slider_fixed_style, mBinding.productSliderContainer, true);
                    itemProductSliderFixedStyleBinding.setData(productSliderData);
                    itemProductSliderFixedStyleBinding.setHandler(new ProductSliderHandler(getContext()));
                    itemProductSliderFixedStyleBinding.productsRv.setLayoutManager(new GridLayoutManager(getContext(), ViewHelper.getSpanCount(getContext())));
                    itemProductSliderFixedStyleBinding.productsRv.setAdapter(new ProductDefaultStyleRvAdapter(getContext(), (ArrayList<ProductData>) productSliderData.getProducts(), SLIDER_MODE_FIXED));
                    break;
            }
        }
    }

    private void getRecentProductList() {
        if (AppSharedPref.isRecentViewEnable(getContext())){
            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(getContext());
            ArrayList<ProductData> productData = sqlLiteDbHelper.getRecentProductList();
             if (productData.size() > 0) {
                    mBinding.llRecentViewProducts.setVisibility(View.VISIBLE);
                    mBinding.rvAlternativeProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    mBinding.rvAlternativeProduct.setAdapter(new AlternativeProductsRvAdapter(getActivity(), productData, true));
             }else {
                    mBinding.llRecentViewProducts.setVisibility(View.GONE);
             }

        } else {
            mBinding.llRecentViewProducts.setVisibility(View.GONE);
        }
    }
}
