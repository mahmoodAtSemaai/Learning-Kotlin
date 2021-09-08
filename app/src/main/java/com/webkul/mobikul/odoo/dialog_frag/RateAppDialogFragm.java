package com.webkul.mobikul.odoo.dialog_frag;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.DialogFragmentRateAppBinding;
import com.webkul.mobikul.odoo.handler.generic.RateAppDialogFragmHandler;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class RateAppDialogFragm extends BaseDialogFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "RateAppDialogFragm";

    public static RateAppDialogFragm newInstance() {
        RateAppDialogFragm rateAppDialogFragm = new RateAppDialogFragm();
        rateAppDialogFragm.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return rateAppDialogFragm;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DialogFragmentRateAppBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_rate_app, container, false);
        binding.setHandler(new RateAppDialogFragmHandler(getContext()));
        return binding.getRoot();
    }

}
