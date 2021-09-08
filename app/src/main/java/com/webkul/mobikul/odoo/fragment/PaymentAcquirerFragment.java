package com.webkul.mobikul.odoo.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.FragmentPaymentMethodBinding;
import com.webkul.mobikul.odoo.handler.checkout.PaymentAcquirerFragmentHandler;
import com.webkul.mobikul.odoo.model.checkout.PaymentAcquirerData;

import java.util.ArrayList;
import java.util.List;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TAG_EXTRA_INFO;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PAYMENT_ACQUIRER_DATA;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class PaymentAcquirerFragment extends BaseFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "PaymentAcquirerFragment";
    public FragmentPaymentMethodBinding mBinding;

    public static PaymentAcquirerFragment newInstance(List<PaymentAcquirerData> paymentAcquirers) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_KEY_PAYMENT_ACQUIRER_DATA, (ArrayList<? extends Parcelable>) paymentAcquirers);

        PaymentAcquirerFragment fragment = new PaymentAcquirerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_method, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<PaymentAcquirerData> paymentDatas = getArguments().getParcelableArrayList(BUNDLE_KEY_PAYMENT_ACQUIRER_DATA);
        if (paymentDatas != null) {
            for (PaymentAcquirerData eachPaymentAcquirerData : paymentDatas) {
                RadioButton eachPaymentMethodRb = new RadioButton(getContext());
                eachPaymentMethodRb.setText(eachPaymentAcquirerData.getName());
                eachPaymentMethodRb.setTag(eachPaymentAcquirerData.getId());
                mBinding.paymentAcquirerRg.addView(eachPaymentMethodRb);
                if (!eachPaymentAcquirerData.getDescription().isEmpty()) {
                    AppCompatTextView extraInfoCompatTextView = new AppCompatTextView(getContext());
                    extraInfoCompatTextView.setText(eachPaymentAcquirerData.getDescription());
                    extraInfoCompatTextView.setTag(eachPaymentAcquirerData.getId() + TAG_EXTRA_INFO);
                    extraInfoCompatTextView.setVisibility(View.GONE);
                    mBinding.paymentAcquirerRg.addView(extraInfoCompatTextView);
                }
            }

            mBinding.paymentAcquirerRg.setOnCheckedChangeListener((group, checkedId) -> {
                String selectedPaymentAcquiredId = group.findViewById(checkedId).getTag().toString();
                String selectedPaymentAcquiredDescriptionTag = selectedPaymentAcquiredId + TAG_EXTRA_INFO;
                for (PaymentAcquirerData eachPaymentAcquirerData : paymentDatas) {
                    String eachViewTag = eachPaymentAcquirerData.getId() + TAG_EXTRA_INFO;
                    if (group.findViewWithTag(eachViewTag) != null) {
                        if (eachViewTag.equals(selectedPaymentAcquiredDescriptionTag)) {
                            group.findViewWithTag(eachViewTag).setVisibility(View.VISIBLE);
                        } else {
                            group.findViewWithTag(eachViewTag).setVisibility(View.GONE);
                        }
                    }
                }
            });

            mBinding.setHandler(new PaymentAcquirerFragmentHandler(getContext()));

        }
    }


}
