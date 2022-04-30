package com.webkul.mobikul.odoo.fragment;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.UpdateAddressActivity;
import com.webkul.mobikul.odoo.adapter.customer.AddressAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentAddressBookBinding;
import com.webkul.mobikul.odoo.handler.customer.AddressRecyclerViewHandler;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.IntentHelper;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddressBookFragment extends BaseFragment implements AddressRecyclerViewHandler.OnAdditionalAddressDeletedListener {

    @SuppressWarnings("unused")
    public final String TAG = "AddressBookFragment";
    private FragmentAddressBookBinding binding;

    public static AddressBookFragment newInstance() {
        return new AddressBookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_address_book, container, false);
        setButtonClickListner();
        return binding.getRoot();
    }

    private void setButtonClickListner() {
        binding.btnConfirmAddress.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), UpdateAddressActivity.class));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }

    public void callApi() {
        ApiConnection.getAddressBookData(getContext(), new BaseLazyRequest(0, 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<MyAddressesResponse>(requireContext()) {
            @Override
            public void onNext(@NonNull MyAddressesResponse myAddressesResponse) {
                super.onNext(myAddressesResponse);
                if (myAddressesResponse.isAccessDenied()) {
                    IntentHelper.redirectToSignUpActivity(requireContext());
                } else {
                    handleAddressResponse(myAddressesResponse);
                }
            }

            @Override
            public void onSubscribe(@androidx.annotation.NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(requireContext());
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

    private void handleAddressResponse(MyAddressesResponse myAddressesResponse) {
        myAddressesResponse.setContext(getContext());
        binding.setData(myAddressesResponse);
        binding.rvAddress.setAdapter(new AddressAdapter(getContext(), myAddressesResponse.getAddresses(), AddressBookFragment.this));
    }

    @Override
    public void onAdditionalAddressDeleted() {
        callApi();
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
