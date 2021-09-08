package com.webkul.mobikul.odoo.dialog_frag;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.generic.QtyAdapter;
import com.webkul.mobikul.odoo.databinding.DialogFragmentChangeQtyBinding;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_QUANTITY;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class ChangeQtyDialogFragment extends BaseDialogFragment {

    @SuppressWarnings("unused")
    private static final String TAG = "ChangeQtyDialogFragment";
    private static OnQtyChangeListener sOnQtyChangeListener;

    public static ChangeQtyDialogFragment newInstance(OnQtyChangeListener onQtyChangeListener, int qty) {
        sOnQtyChangeListener = onQtyChangeListener;
        Bundle args = new Bundle();
        args.putInt(BUNDLE_KEY_QUANTITY, qty);
        ChangeQtyDialogFragment fragment = new ChangeQtyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DialogFragmentChangeQtyBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_change_qty, container, false);

        /*this piece of code is not working to set max height of the dialog..*/
        int height = getResources().getDimensionPixelSize(R.dimen.max_qty_dialog_height);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        }

        binding.qtyRv.setAdapter(new QtyAdapter(getContext(), sOnQtyChangeListener, getArguments().getInt(BUNDLE_KEY_QUANTITY)));
        return binding.getRoot();
    }

    public interface OnQtyChangeListener {
        void onQtyChanged(int qty);
    }
}
