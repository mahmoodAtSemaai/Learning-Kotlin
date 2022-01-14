package com.webkul.mobikul.odoo.handler.checkout;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.fragment.BillingShippingFragment;
import com.webkul.mobikul.odoo.fragment.NewAddressFragment;
import com.webkul.mobikul.odoo.fragment.PaymentAcquirerFragment;
import com.webkul.mobikul.odoo.fragment.ShippingMethodFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.model.checkout.PaymentAcquirerResponse;
import com.webkul.mobikul.odoo.model.checkout.ShippingMethodResponse;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
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

public class BillingShippingFragmentHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "BillingShippingFragment";
    private Context mContext;
    private MyAddressesResponse mData;
    private ArrayList<Boolean> isAddressValid = new ArrayList<>();

    public BillingShippingFragmentHandler(Context context, MyAddressesResponse myAddressesResponse) {
        mContext = context;
        mData = myAddressesResponse;
        setAddressValidationArray(myAddressesResponse);
    }


    public void editBillingAddress() {
        FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(String.valueOf(mData.getAddresses().get(0).getUrl()), mContext.getString(R.string.edit_billing_address)
                , NewAddressFragment.AddressType.TYPE_BILLING), NewAddressFragment.class.getSimpleName(), true, false);
    }

    public void editShippingAddress() {
        Fragment fragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(BillingShippingFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            int selectedShippingAddressPosn = ((BillingShippingFragment) fragment).mBinding.shippingAddressSpinner.getSelectedItemPosition();
            FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(String.valueOf(mData.getAddresses().get(selectedShippingAddressPosn).getUrl()), mContext.getString(R.string.edit_shipping_address)
                    , NewAddressFragment.AddressType.TYPE_SHIPPING), NewAddressFragment.class.getSimpleName(), true, false);
        }

    }

    public void addNewAddress() {
        FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(null, mContext.getString(R.string.add_new_address), NewAddressFragment.AddressType.TYPE_NEW), NewAddressFragment.class.getSimpleName(), true, false);
    }

    public void loadShippingMethods() {
        if (AppSharedPref.isAllowShipping(mContext)) {
            validateSelectedAddress();
        } else {
            fetchPaymentAquirers();
        }
    }

    private void validateSelectedAddress() {
        Fragment fragment = getBillingFragment();
        if (fragment != null && fragment.isAdded()) {
            int selectedShippingAddressPosition = ((BillingShippingFragment) fragment).mBinding.shippingAddressSpinner.getSelectedItemPosition();
            if (isAddressValid.get(selectedShippingAddressPosition)) {
                fetchShippingMethods();
            } else {
                AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.service_unavailable), mContext.getString(R.string.service_unavailablity_text));
            }
        }
    }

    private Fragment getBillingFragment() {
        Fragment fragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(BillingShippingFragment.class.getSimpleName());
        return fragment;
    }


    private void fetchPaymentAquirers() {
        ApiConnection.getPaymentAcquirers(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getPaymentAquirerResponseObserver());
    }

    private Observer<? super PaymentAcquirerResponse> getPaymentAquirerResponseObserver() {
        return new CustomObserver<PaymentAcquirerResponse>(mContext) {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(mContext);
            }

            @Override
            public void onNext(@NonNull PaymentAcquirerResponse paymentAcquirerResponse) {
                super.onNext(paymentAcquirerResponse);
                handlePaymentAcquirerResponse(paymentAcquirerResponse);
            }
        };
    }

    private void handlePaymentAcquirerResponse(PaymentAcquirerResponse paymentAcquirerResponse) {
        if (paymentAcquirerResponse.isAccessDenied()) {
            redirectToSignInSignUp();
        } else {
            if (paymentAcquirerResponse.isSuccess()) {
                FragmentHelper.addFragment(R.id.container, mContext, PaymentAcquirerFragment.newInstance(paymentAcquirerResponse.getPaymentAcquirers()), PaymentAcquirerFragment.class.getSimpleName(), true, true);
            } else {
                AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), paymentAcquirerResponse.getMessage());
            }
        }
    }

    private void fetchShippingMethods() {
        ApiConnection.getActiveShippings(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getShippingMethodResponseObserver());
    }

    private Observer<? super ShippingMethodResponse> getShippingMethodResponseObserver() {
        return new CustomObserver<ShippingMethodResponse>(mContext) {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(mContext);
            }

            @Override
            public void onNext(@NonNull ShippingMethodResponse shippingMethodResponse) {
                super.onNext(shippingMethodResponse);
                if (shippingMethodResponse.isAccessDenied()) {
                    redirectToSignInSignUp();
                } else {
                    handleShippingMethodResponse(shippingMethodResponse);
                }
            }
        };
    }

    private void handleShippingMethodResponse(ShippingMethodResponse shippingMethodResponse) {
        if (shippingMethodResponse.isSuccess()) {
            FragmentHelper.addFragment(R.id.container, mContext, ShippingMethodFragment.newInstance(shippingMethodResponse.getShippingMethods()), ShippingMethodFragment.class.getSimpleName(), true, true);
        } else {
            AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), shippingMethodResponse.getMessage());
        }
    }

    private void setAddressValidationArray(MyAddressesResponse myAddressesResponse) {
        List<String> addressDisplayNames = myAddressesResponse.getAddressesNames();
        for (int index = 0; index < addressDisplayNames.size(); index++) {
            if (index == 0 && addressDisplayNames.get(index).equalsIgnoreCase(mContext.getString(R.string.billing_address_place_holder)))
                isAddressValid.add(index, false);
            else if (index != 0 && addressDisplayNames.get(index).isEmpty())
                isAddressValid.add(index, false);
            else
                isAddressValid.add(true);
        }
    }

    private void redirectToSignInSignUp() {
        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), (SweetAlertDialog.OnSweetClickListener) sweetAlertDialog -> {
            sweetAlertDialog.dismiss();
            AppSharedPref.clearCustomerData(mContext);
            Intent signInSignUpIntent = new Intent(mContext, SignInSignUpActivity.class);
            signInSignUpIntent.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, mContext.getClass().getSimpleName());
            mContext.startActivity(signInSignUpIntent);
        });
    }
}
