package com.webkul.mobikul.odoo.fragment;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentBillingShippingBinding;
import com.webkul.mobikul.odoo.handler.checkout.BillingShippingFragmentHandler;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;

import java.util.ArrayList;
import java.util.List;

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


public class BillingShippingFragment extends BaseFragment {
    public static final String TAG = "BillingShippingFragment";
    public FragmentBillingShippingBinding mBinding;

    public ArrayList<Boolean> isAddressValid = new ArrayList<>();

    public static BillingShippingFragment newInstance() {
        return new BillingShippingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_billing_shipping, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchAllAddresses();
    }

    private void fetchAllAddresses() {
        ApiConnection.getAddressBookData(getContext(), new BaseLazyRequest(0, 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getAddressResponseObserver());
    }

    private Observer<? super MyAddressesResponse> getAddressResponseObserver() {
        return new CustomObserver<MyAddressesResponse>(getContext()) {
            @Override
            public void onNext(@NonNull MyAddressesResponse myAddressesResponse) {
                super.onNext(myAddressesResponse);
                if (myAddressesResponse.isAccessDenied()) {
                    redirectToSignInSignUp();
                } else {
                    setAddressValidationArray(myAddressesResponse);
                    if (myAddressesResponse.getAddresses().get(0).getDisplayName().replaceAll("\\n", "").trim().isEmpty()) {
                        showEmptyBillingAddressPrompt(myAddressesResponse);
                    } else {
                        setValidAddressesData(myAddressesResponse);
                    }
                }
            }
        };
    }

    private void setValidAddressesData(MyAddressesResponse myAddressesResponse) {
        myAddressesResponse.setContext(getContext());
        mBinding.setData(myAddressesResponse);
        setAddressSpinnerAdapter(myAddressesResponse);
        if (myAddressesResponse.getAddresses().size() > 1) {
            mBinding.shippingAddressSpinner.setSelection(1);
        }
        mBinding.setHandler(new BillingShippingFragmentHandler(getContext(), myAddressesResponse));
    }

    private void setAddressSpinnerAdapter(MyAddressesResponse myAddressesResponse) {
        ArrayAdapter<String> shippingAddressAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_multiline_dropdown_item, myAddressesResponse.getAddressesNames());
        shippingAddressAdapter.setDropDownViewResource(R.layout.simple_spinner_multiline_dropdown_item);
        mBinding.shippingAddressSpinner.setAdapter(shippingAddressAdapter);
    }

    private void showEmptyBillingAddressPrompt(MyAddressesResponse myAddressesResponse) {
        ((BaseActivity) getContext()).mSweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getContext().getString(R.string.error_default_address_not_found))
                .setContentText(getContext().getString(R.string.question_add_new_address_to_place_order))
                .setConfirmText(getContext().getString(R.string.ok))
                .setConfirmClickListener(sweetAlertDialog -> {
                    FragmentHelper.replaceFragment(R.id.container, getContext(), NewAddressFragment.newInstance(String.valueOf(myAddressesResponse.getAddresses().get(0).getUrl()), getContext().getString(R.string.edit_billing_address)
                            , NewAddressFragment.AddressType.TYPE_BILLING_CHECKOUT), NewAddressFragment.class.getSimpleName(), true, false);
                    sweetAlertDialog.dismiss();
                })
                .setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    getActivity().finish();

                });
        ((BaseActivity) getContext()).mSweetAlertDialog.show();
        ((BaseActivity) getContext()).mSweetAlertDialog.showCancelButton(true);
    }

    private void setAddressValidationArray(MyAddressesResponse myAddressesResponse) {
        List<String> addressDisplayNames = myAddressesResponse.getAddressesNames();
        for (int index = 0; index < addressDisplayNames.size(); index++) {
            if (index == 0 && addressDisplayNames.get(index).equalsIgnoreCase(getString(R.string.billing_address_place_holder)))
                isAddressValid.add(index, false);
            else if (index != 0 && addressDisplayNames.get(index).isEmpty())
                isAddressValid.add(index, false);
            else
                isAddressValid.add(true);
        }
    }

    private void redirectToSignInSignUp() {
        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                AppSharedPref.clearCustomerData(getContext());
                Intent signInSignUpIntent = new Intent(getContext(), SignInSignUpActivity.class);
                signInSignUpIntent.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, getActivity().getClass().getSimpleName());
                startActivity(signInSignUpIntent);
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
