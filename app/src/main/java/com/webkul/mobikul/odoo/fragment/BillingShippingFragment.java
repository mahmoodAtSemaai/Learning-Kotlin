package com.webkul.mobikul.odoo.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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


public class BillingShippingFragment extends BaseFragment {
    public FragmentBillingShippingBinding mBinding;

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
        ApiConnection.getAddressBookData(getContext(), new BaseLazyRequest(0, 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<MyAddressesResponse>(getContext()) {

            @Override
            public void onNext(@NonNull MyAddressesResponse myAddressesResponse) {
                super.onNext(myAddressesResponse);
                if (myAddressesResponse.isAccessDenied()){
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
                }else {
                    if (myAddressesResponse.getAddresses().get(0).getDisplayName().replaceAll("\\n", "").trim().isEmpty()) {
                        ((BaseActivity) getContext()).mSweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getContext().getString(R.string.error_default_address_not_found))
                                .setContentText(getContext().getString(R.string.question_add_new_address_to_place_order))
                                .setConfirmText(getContext().getString(R.string.ok))
                                .setConfirmClickListener(sweetAlertDialog -> {
                                    // reuse previous dialog instance
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
                    } else {
                        myAddressesResponse.setContext(getContext());
                        mBinding.setData(myAddressesResponse);
                        ArrayAdapter<String> shippingAddressAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_multiline_dropdown_item, myAddressesResponse.getAddressesNames());
                        shippingAddressAdapter.setDropDownViewResource(R.layout.simple_spinner_multiline_dropdown_item);
                        mBinding.shippingAddressSpinner.setAdapter(shippingAddressAdapter);
                        /*there might be possibility that there is only one address for the new customer i.e. billing address thus we cannot make the selection...*/
                        if (myAddressesResponse.getAddresses().size() > 1) {
                            mBinding.shippingAddressSpinner.setSelection(1);
                        }

                        mBinding.setHandler(new BillingShippingFragmentHandler(getContext(), myAddressesResponse));
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
}
