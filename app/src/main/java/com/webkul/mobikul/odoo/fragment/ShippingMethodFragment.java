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
import com.webkul.mobikul.odoo.databinding.FragmentShippingMethodBinding;
import com.webkul.mobikul.odoo.handler.checkout.ShippingMethodFragmentHandler;
import com.webkul.mobikul.odoo.model.checkout.ActiveShippingMethod;

import java.util.ArrayList;
import java.util.List;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TAG_EXTRA_INFO;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_ACTIVE_SHIPPING_DATA;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class ShippingMethodFragment extends BaseFragment {

    public FragmentShippingMethodBinding mBinding;

    public static ShippingMethodFragment newInstance(List<ActiveShippingMethod> shippingMethods) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_KEY_ACTIVE_SHIPPING_DATA, (ArrayList<? extends Parcelable>) shippingMethods);

        ShippingMethodFragment fragment = new ShippingMethodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shipping_method, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<ActiveShippingMethod> shippingMethodList = getArguments().getParcelableArrayList(BUNDLE_KEY_ACTIVE_SHIPPING_DATA);
        if (shippingMethodList != null) {
            for (ActiveShippingMethod shippingMethod : shippingMethodList) {
                RadioButton eachShippingMethodRb = new RadioButton(getContext());
//                if(Double.parseDouble(shippingMethod.getPrice())>0)
//                {
                eachShippingMethodRb.setText(String.format("%s (%s)", shippingMethod.getName(), shippingMethod.getPrice()));
//                }
//                else
//                {
//                    eachShippingMethodRb.setText(shippingMethod.getName());
//                }

                eachShippingMethodRb.setTag(shippingMethod.getId());
                mBinding.shippingMethodRg.addView(eachShippingMethodRb);
                if (!shippingMethod.getDescription().isEmpty()) {
                    AppCompatTextView extraInfoCompatTextView = new AppCompatTextView(getContext());
                    extraInfoCompatTextView.setText(shippingMethod.getDescription());
                    extraInfoCompatTextView.setTag(shippingMethod.getId() + TAG_EXTRA_INFO);
                    extraInfoCompatTextView.setVisibility(View.GONE);
                    mBinding.shippingMethodRg.addView(extraInfoCompatTextView);
                }
            }

            mBinding.shippingMethodRg.setOnCheckedChangeListener((group, checkedId) -> {
                String selectedShippingMethodId = group.findViewById(checkedId).getTag().toString();
                String selectedShippingMethodDescriptionTag = selectedShippingMethodId + TAG_EXTRA_INFO;
                for (ActiveShippingMethod shippingMethod : shippingMethodList) {
                    String eachViewTag = shippingMethod.getId() + TAG_EXTRA_INFO;
                    if (group.findViewWithTag(eachViewTag) != null) {
                        if (eachViewTag.equals(selectedShippingMethodDescriptionTag)) {
                            group.findViewWithTag(eachViewTag).setVisibility(View.VISIBLE);
                        } else {
                            group.findViewWithTag(eachViewTag).setVisibility(View.GONE);
                        }
                    }
                }
            });

            mBinding.setHandler(new ShippingMethodFragmentHandler(getContext()));

        }
    }
}
