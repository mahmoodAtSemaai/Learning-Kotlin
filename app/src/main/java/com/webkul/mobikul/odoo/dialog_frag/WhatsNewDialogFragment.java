package com.webkul.mobikul.odoo.dialog_frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.DialogFragmentRateAppBinding;
import com.webkul.mobikul.odoo.databinding.DialogFragmentWhatsNewBinding;
import com.webkul.mobikul.odoo.handler.generic.RateAppDialogFragmHandler;
import com.webkul.mobikul.odoo.helper.AppSharedPref;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

public class WhatsNewDialogFragment extends BaseDialogFragment{
    @SuppressWarnings("unused")
    private static final String TAG = "WhatsNewDialogFragment";

    public static WhatsNewDialogFragment newInstance() {
        WhatsNewDialogFragment whatsNewDialogFragment = new WhatsNewDialogFragment();
        whatsNewDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        whatsNewDialogFragment.setCancelable(false);
        return whatsNewDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DialogFragmentWhatsNewBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_whats_new, container, false);

        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSharedPref.setIsLoyaltyPointsDialogActive(getContext(), false);
                dismiss();
            }
        });

        return binding.getRoot();
    }
}
