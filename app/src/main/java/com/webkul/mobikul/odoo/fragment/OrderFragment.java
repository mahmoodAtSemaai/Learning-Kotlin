package com.webkul.mobikul.odoo.fragment;


import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.adapter.customer.OrderDetailProductInfoRvAdapter;
import com.webkul.mobikul.odoo.adapter.customer.OrderDetailTransactionRvAdapter;
import com.webkul.mobikul.odoo.adapter.customer.OrderDetailsDeliveryOrderRvAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentOrderBinding;
import com.webkul.mobikul.odoo.handler.order.OrderFragmentHandler;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.customer.order.OrderDetailResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_URL;



/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class OrderFragment extends BaseFragment {

    private FragmentOrderBinding mBinding;

    public static final String TAG = "OrderFragment";

    public static OrderFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_URL, url);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ApiConnection.getOrderDetailsData(getContext(), getArguments().getString(BUNDLE_KEY_URL)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<OrderDetailResponse>(getContext()) {

            @Override
            public void onNext(@NonNull OrderDetailResponse orderDetailResponse) {
                super.onNext(orderDetailResponse);

                if (orderDetailResponse.isAccessDenied()) {
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
                    mBinding.setData(orderDetailResponse);
                    mBinding.setHandler(new OrderFragmentHandler(getContext()));
                    mBinding.executePendingBindings();
                    DividerItemDecoration dividerItemDecorationHorizontal = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
                    mBinding.productInfoRv.addItemDecoration(dividerItemDecorationHorizontal);
                    mBinding.productInfoRv.setAdapter(new OrderDetailProductInfoRvAdapter(getContext(), orderDetailResponse.getItems()));
                    mBinding.transactionsRv.setAdapter(new OrderDetailTransactionRvAdapter(getContext(),orderDetailResponse.getTransactions()));
                    mBinding.deliveryOrdersRv.setAdapter(new OrderDetailsDeliveryOrderRvAdapter(getContext(), orderDetailResponse.getDeliveryOrders()));
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Helper.hiderAllMenuItems(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            mBinding.getHandler().onClickDownloadDeliveryOrder(mBinding.getData().getDeliveryOrders().get(0).getReportUrl(), mBinding.getData().getName());
        }
    }

    @androidx.annotation.NonNull
    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void setTitle(@androidx.annotation.NonNull String title) {

    }
}
