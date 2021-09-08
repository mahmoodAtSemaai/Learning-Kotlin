package com.webkul.mobikul.odoo.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.adapter.catalog.OrderRvAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentOrderListBinding;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.customer.order.MyOrderReponse;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;



/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class OrderListFragment extends BaseFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderListFragment";
    private FragmentOrderListBinding mBinding;
    private boolean mIsFirstCall = true;
    private int mOffset;


    public static OrderListFragment newInstance() {
        return new OrderListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*INTIALIZE */
        mIsFirstCall = true;
        mOffset = 0;
        callApi();
    }

    private void callApi() {
        ApiConnection.getOrders(getContext(), new BaseLazyRequest(mOffset, AppSharedPref.getItemsPerPage(getContext()))).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<MyOrderReponse>(getContext()) {

            @Override
            public void onNext(@NonNull MyOrderReponse myOrderReponse) {
                super.onNext(myOrderReponse);
                if (myOrderReponse.isAccessDenied()) {
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
                    if (mIsFirstCall) {
                        mIsFirstCall = false;
                        mBinding.setData(myOrderReponse);

                        /*BETTER REPLACE SOME CONTAINER INSTEAD OF WHOLE PAGE android.R.id.content */
                        if (mBinding.getData().getOrders().isEmpty()) {
                            FragmentHelper.replaceFragment(R.id.container, getContext(), EmptyFragment.newInstance(R.drawable.ic_vector_empty_order, getString(R.string.no_order_placed), "", true,
                                    EmptyFragment.EmptyFragType.TYPE_ORDER.ordinal()), EmptyFragment.class.getSimpleName(), false, false);
                        } else {
                            mBinding.orderRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                                    /*Logic is working fine for lazy loading*/
                                    if (!mBinding.getData().isLazyLoading() && lastCompletelyVisibleItemPosition == mBinding.orderRv.getAdapter().getItemCount() - 1 && mBinding.orderRv.getAdapter().getItemCount() < mBinding.getData().getTotalCount()) {
                                        mOffset += AppSharedPref.getItemsPerPage(getContext());
                                        callApi();
                                        mBinding.getData().setLazyLoading(true);
                                    }
                                }
                            });
                            mBinding.orderRv.setAdapter(new OrderRvAdapter(getContext(), mBinding.getData().getOrders()));
                        }
                    } else {
                        mBinding.getData().setLazyLoading(false);
                        mBinding.getData().getOrders().addAll(myOrderReponse.getOrders());
                        mBinding.orderRv.getAdapter().notifyDataSetChanged();
                    }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Helper.hiderAllMenuItems(menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
