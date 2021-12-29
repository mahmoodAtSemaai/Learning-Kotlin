package com.webkul.mobikul.odoo.fragment;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.SLIDER_MODE_DEFAULT;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.SLIDER_MODE_FIXED;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.NewAddressActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
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
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.ViewHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressData;
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse;
import com.webkul.mobikul.odoo.model.generic.ProductData;
import com.webkul.mobikul.odoo.model.generic.ProductSliderData;
import com.webkul.mobikul.odoo.model.generic.StateData;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


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
public class HomeFragment extends BaseFragment implements CustomRetrofitCallback {

    @SuppressWarnings("unused")
    private static final String TAG = "HomeFragment";
    public FragmentHomeBinding mBinding;

    private int CHECK_FOR_EXISTING_ADDRESS = 1;
    private int BILLING_ADDRESS_POSITION = 1;
    private int COMPANY_ID = 1;

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
        fetchExistingAddresses();
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
        if (isFromApi) {
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
        if (AppSharedPref.isRecentViewEnable(getContext())) {
            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(getContext());
            ArrayList<ProductData> productData = sqlLiteDbHelper.getRecentProductList();
            if (productData.size() > 0) {
                mBinding.llRecentViewProducts.setVisibility(View.VISIBLE);
                mBinding.rvAlternativeProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                mBinding.rvAlternativeProduct.setAdapter(new AlternativeProductsRvAdapter(getActivity(), productData, true));
            } else {
                mBinding.llRecentViewProducts.setVisibility(View.GONE);
            }

        } else {
            mBinding.llRecentViewProducts.setVisibility(View.GONE);
        }
    }

    public void fetchExistingAddresses() {
        ApiConnection.getAddressBookData(getContext(), new BaseLazyRequest(0, 1)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getAddressResponseObserver());
    }

    private Observer<? super MyAddressesResponse> getAddressResponseObserver() {
        return new CustomObserver<MyAddressesResponse>(getContext()) {
            @Override
            public void onNext(@NonNull MyAddressesResponse myAddressesResponse) {
                super.onNext(myAddressesResponse);
                checkIfAddressExists(myAddressesResponse);
            }
        };
    }

    private void checkIfAddressExists(MyAddressesResponse myAddressesResponse) {
        if (myAddressesResponse.isAccessDenied()) {
            showAlertDialog(getString(R.string.error_login_failure), getString(R.string.access_denied_message));
        } else {
            myAddressesResponse.setContext(getContext());
            if (myAddressesResponse.getAddresses().size() == 0) {
                showMissingBillingAddressPrompt();
            } else
                fetchBillingAddressData(myAddressesResponse.getAddresses().get(0));
        }
    }

    private void showMissingBillingAddressPrompt() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.billing_address_missing))
                .setContentText(getString(R.string.no_address_text))
                .setConfirmText(getString(R.string.add_now))
                .setConfirmClickListener(sweetAlertDialog -> {
                    redirectToAddNewBillingAddress();
                    sweetAlertDialog.dismiss();
                })
                .show();
    }

    private void redirectToAddNewBillingAddress() {
        startActivity(new Intent(requireActivity(), NewAddressActivity.class));
    }

    private void fetchBillingAddressData(AddressData addressData) {
        ApiConnection.getAddressFormData(getContext(), addressData.getUrl()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getBillingAddressResponse(addressData));
    }

    private Observer<? super AddressFormResponse> getBillingAddressResponse(AddressData addressData) {
        return new CustomObserver<AddressFormResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull AddressFormResponse addressFormResponse) {
                super.onNext(addressFormResponse);
                fetchStates(addressFormResponse, addressData);
            }
        };
    }

    private void fetchStates(AddressFormResponse addressFormResponse, AddressData addressData) {
        ApiConnection.getStates(requireContext(), COMPANY_ID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getStateListResponse(addressFormResponse, addressData));
    }

    private Observer<? super StateListResponse> getStateListResponse(AddressFormResponse addressFormResponse, AddressData addressData) {
        return new CustomObserver<BaseResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull BaseResponse baseResponse) {
                super.onNext(baseResponse);
                StateListResponse stateListResponse = (StateListResponse) baseResponse;
                checkForStateAvailablity(stateListResponse, addressFormResponse, addressData);
            }
        };
    }

    private void checkForStateAvailablity(StateListResponse stateListResponse, AddressFormResponse addressFormResponse, AddressData addressData) {
        for (StateData stateData : stateListResponse.getResult()) {
            if (stateData.isAvailable() && (String.valueOf(stateData.getId()).equals(addressFormResponse.getStateId()) || isStateDataMissing(addressFormResponse)) &&
                    areFeildsNullOrEmpty(addressFormResponse)) {
                showPromptToCompleteBillingAddress(addressFormResponse, addressData);
            }
        }
    }

    private boolean isStateDataMissing(AddressFormResponse addressFormResponse) {
        return (addressFormResponse.getStateId() == null || addressFormResponse.getStateId().isEmpty());
    }

    private void showPromptToCompleteBillingAddress(AddressFormResponse addressFormResponse, AddressData addressData) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.billing_address_incomplete))
                .setContentText(getString(R.string.no_address_text))
                .setConfirmText(getString(R.string.add_now))
                .setConfirmClickListener(sweetAlertDialog -> {
                    redirectToEditExistingBillingAddress(addressData);
                    sweetAlertDialog.dismiss();
                })
                .show();
    }

    private boolean areFeildsNullOrEmpty(AddressFormResponse addressFormResponse) {
        return (addressFormResponse.getDistrictId() == null || addressFormResponse.getDistrictId().isEmpty() ||
                addressFormResponse.getSub_district_id() == null || addressFormResponse.getSub_district_id().isEmpty() ||
                addressFormResponse.getVillage_id() == null || addressFormResponse.getVillage_id().isEmpty()
        );
    }

    private void redirectToEditExistingBillingAddress(AddressData addressData) {
        startActivity(new Intent(requireActivity(), NewAddressActivity.class)
                .putExtra(BUNDLE_KEY_URL, addressData.getUrl()));
    }


    private void showAlertDialog(String title, String message) {
        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), title, message, sweetAlertDialog -> {
            sweetAlertDialog.dismiss();
            clearCustomerDataFromSharedPref();
            redirectToCatalogActivity();
        });
    }

    private void redirectToCatalogActivity() {
        Intent i = new Intent(getContext(), SignInSignUpActivity.class);
        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, CatalogProductActivity.class.getSimpleName());
        startActivity(i);
    }

    private void clearCustomerDataFromSharedPref() {
        AppSharedPref.clearCustomerData(getContext());
    }


}
