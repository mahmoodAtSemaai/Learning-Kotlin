package com.webkul.mobikul.odoo.dialog_frag;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.DialogFragmentChangeDefaultShippingBinding;
import com.webkul.mobikul.odoo.handler.customer.ChangeDefaultShippingDialogFragHandler;
import com.webkul.mobikul.odoo.helper.ViewHelper;
import com.webkul.mobikul.odoo.model.customer.address.AddressData;
import com.webkul.mobikul.odoo.model.customer.dashboard.DashboardData;

import java.util.ArrayList;
import java.util.List;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_ADDRESSES;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_DASHBOARD_DATA;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class ChangeDefaultShippingDialogFrag extends BaseDialogFragment {

    @SuppressWarnings("unused")
    private static final String TAG = "ChangeDefaultShippingDi";
    public DialogFragmentChangeDefaultShippingBinding mBinding;

    public static ChangeDefaultShippingDialogFrag newInstance(DashboardData data) {
        ChangeDefaultShippingDialogFrag changeDefaultShippingDialogFrag = new ChangeDefaultShippingDialogFrag();
        changeDefaultShippingDialogFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_KEY_ADDRESSES, (ArrayList<? extends Parcelable>) data.getAddresses());
        args.putParcelable(BUNDLE_KEY_DASHBOARD_DATA, data);
        changeDefaultShippingDialogFrag.setArguments(args);
        return changeDefaultShippingDialogFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_change_default_shipping, container, false);
        List<AddressData> addresses = getArguments().getParcelableArrayList(BUNDLE_KEY_ADDRESSES);
        if (addresses != null) {
            for (int addressIdx = 0; addressIdx < addresses.size(); addressIdx++) {
                AddressData eachAddressData = addresses.get(addressIdx);
                AppCompatRadioButton eachAddressRb = new AppCompatRadioButton(getContext());
                eachAddressRb.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT));
                eachAddressRb.setPadding(ViewHelper.dpToPx(getContext(), 8), ViewHelper.dpToPx(getContext(), 8), ViewHelper.dpToPx(getContext(), 8), ViewHelper.dpToPx(getContext(), 8));
                eachAddressRb.setText(eachAddressData.getDisplayName());
                eachAddressRb.setId(addressIdx);
                eachAddressRb.setTag(eachAddressData.getUrl());
                if (addressIdx == 1) {
                    eachAddressRb.setChecked(true);
                }

                mBinding.selectShippingRg.addView(eachAddressRb);
                if (addressIdx < addresses.size() - 1) {
                    DataBindingUtil.inflate(inflater, R.layout.divider_horizontal, mBinding.selectShippingRg, true);
                }
            }
            mBinding.setHandler(new ChangeDefaultShippingDialogFragHandler(getContext(), getArguments().getParcelable(BUNDLE_KEY_DASHBOARD_DATA)));
        }
        return mBinding.getRoot();
    }
}
