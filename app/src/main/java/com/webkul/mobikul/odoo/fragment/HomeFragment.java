package com.webkul.mobikul.odoo.fragment;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.NewHomeActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.activity.UpdateAddressActivity;
import com.webkul.mobikul.odoo.adapter.home.CategoryProductAdapter;
import com.webkul.mobikul.odoo.adapter.home.FeaturedCategoriesAdapter;
import com.webkul.mobikul.odoo.adapter.home.HomeBannerAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.connection.CustomRetrofitCallback;
import com.webkul.mobikul.odoo.constant.BundleConstant;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.database.SaveData;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.FragmentHomeBinding;
import com.webkul.mobikul.odoo.handler.home.FragmentNotifier;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.NetworkHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressData;
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse;
import com.webkul.mobikul.odoo.model.generic.FeaturedCategoryData;
import com.webkul.mobikul.odoo.model.generic.StateData;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends BaseFragment implements CustomRetrofitCallback {

    @SuppressWarnings("unused")
    private static final String TAG = "HomeFragment";
    public FragmentHomeBinding binding;

    private int CHECK_FOR_EXISTING_ADDRESS = 1;
    private int BILLING_ADDRESS_POSITION = 1;
    private int COMPANY_ID = 1;
    private boolean mIsFirstCall = true;
    private CustomToast mToast;
    public static final int VIEW_TYPE_LIST = 1;
    Handler handler = new Handler();
    Runnable runnable;
    public static final int VIEW_TYPE_GRID = 2;
    private FeaturedCategoryData mFeaturedCategoryData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.hideKeyboard(getContext());



        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                binding.refreshLayout.setEnabled(verticalOffset == 0);
            }
        });

        binding.refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext() , R.color.background_orange));

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.refreshLayout.setRefreshing(true);
                hitApiForFetchingData();
            }
        });


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HomePageResponse homePageResponse = ((NewHomeActivity) getActivity()).getHomePageResponse();
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
                if(binding.refreshLayout.isEnabled() && binding.refreshLayout.isRefreshing())
                    binding.refreshLayout.setRefreshing(false);
            }
        });

    }


    @Override
    public void onSuccess(Object object) {
    }

    @Override
    public void onError(Throwable t) {

    }

    private void loadHomePage(HomePageResponse homePageResponse, boolean isFromApi) {
        if (isFromApi) {
            homePageResponse.updateSharedPref(getContext(), "");
        }
        binding.setData(homePageResponse);

        binding.featuredCategoriesRv.setAdapter(new FeaturedCategoriesAdapter(getContext(), homePageResponse.getFeaturedCategories(), value));


        List<FeaturedCategoryData> fragment = homePageResponse.getFeaturedCategories();

        CategoryProductAdapter adapter = new CategoryProductAdapter((NewHomeActivity) requireContext(), fragment);
        binding.viewPager2.setUserInputEnabled(false);
        binding.viewPager2.setAdapter(adapter);

        binding.bannerViewPager.setAdapter(new HomeBannerAdapter(getContext(), homePageResponse.getBannerImages() , binding.bannerViewPager));


        binding.bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0){
                    handler.postDelayed(runnable, 5000);
                }
            }

            @Override
            public void onPageSelected(int position) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 5000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        runnable  = () -> binding.bannerViewPager.setCurrentItem(binding.bannerViewPager.getCurrentItem()+1);


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
        startActivity(new Intent(requireActivity(), UpdateAddressActivity.class));
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
        startActivity(new Intent(requireActivity(), UpdateAddressActivity.class)
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(getContext());
        EventBus.getDefault().post(FragmentNotifier.HomeActivityFragments.HOME_FRAGMENT);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(getContext());
    }

    @androidx.annotation.NonNull
    @Override
    public String getTitle() {
        return TAG;
    }

    @Override
    public void setTitle(@androidx.annotation.NonNull String title) {

    }


    FeaturedCategoriesAdapter.FeaturedCategoryDataValue value = new FeaturedCategoriesAdapter.FeaturedCategoryDataValue() {
        @Override
        public void data(FeaturedCategoryData featuredCategoryData, Integer pos) {
            mFeaturedCategoryData = featuredCategoryData;
            binding.viewPager2.setCurrentItem(pos);
        }
    };

}
