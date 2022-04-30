package com.webkul.mobikul.odoo.fragment;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.adapter.catalog.OrdersAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentDashboardBinding;
import com.webkul.mobikul.odoo.handler.customer.DashboardFragmentHandler;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;
import com.webkul.mobikul.odoo.model.customer.dashboard.DashboardData;
import com.webkul.mobikul.odoo.model.customer.order.MyOrderReponse;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

public class DashboardFragment extends BaseFragment {

    @SuppressWarnings("unused")
    private static final String TAG = "DashboardFragment";
    private FragmentDashboardBinding binding;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setCustomerName(Helper.initCap(AppSharedPref.getCustomerName(getContext())));
        binding.setCustomerEmail(AppSharedPref.getCustomerEmail(getContext()));
        binding.setCustomerPhoneNumber(AppSharedPref.getCustomerPhoneNumber(getContext()));
        Observable<MyOrderReponse> myOrderReponseObservable = ApiConnection.getOrders(getContext(), new BaseLazyRequest(0, 5)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<MyAddressesResponse> myAddressesResponseDataObservable = ApiConnection.getAddressBookData(getContext(), new BaseLazyRequest(0, 1)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Observable.zip(myOrderReponseObservable, myAddressesResponseDataObservable, (myOrderReponse, myAddressesResponseData) -> {
            DashboardData dashboardData = new DashboardData(getContext());
            dashboardData.setRecentOrders(myOrderReponse.getOrders());
            dashboardData.setEmailVerified(myOrderReponse.isEmailVerified());
            dashboardData.setAddons(myOrderReponse.getAddons());
            dashboardData.setItemsPerPage(myOrderReponse.getItemsPerPage());
            dashboardData.setCartCount(myOrderReponse.getCartCount());
            myAddressesResponseData.setContext(getContext());
            dashboardData.setAddresses(myAddressesResponseData.getAddresses());
            dashboardData.setDefaultShippingAddress(myAddressesResponseData.getDefaultShippingAddress());
            if (myOrderReponse.isAccessDenied()) {
                dashboardData.setAccessDenied(true);
            }

            if (myAddressesResponseData.isAccessDenied()) {
                dashboardData.setAccessDenied(true);
            }


            return dashboardData;
        }).subscribe(new CustomObserver<DashboardData>(getContext()) {

            @Override
            public void onComplete() {
                binding.setHandler(new DashboardFragmentHandler(getContext(), binding.getData()));
            }

            @Override
            public void onNext(@NonNull DashboardData dashboardData) {
                super.onNext(dashboardData);
                if (dashboardData.isAccessDenied()) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(getContext());
                            Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, getActivity().getClass().getSimpleName());
                            startActivity(i);
                        }
                    });
                } else {
                    binding.setData(dashboardData);
                    binding.recentOrderRv.setAdapter(new OrdersAdapter(getContext(), binding.getData().getRecentOrders(), TAG));
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
            }
        });
    }
    @androidx.annotation.NonNull
    @Override
    public String getTitle() {
        return TAG;
    }

    @Override
    public void setTitle(@androidx.annotation.NonNull String title) {

    }
}
