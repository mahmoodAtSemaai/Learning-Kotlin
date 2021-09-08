package com.webkul.mobikul.odoo.dialog_frag;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.DialogFragmentForgotPasswordBinding;
import com.webkul.mobikul.odoo.handler.customer.ForgotPasswordDialogHandler;
import com.webkul.mobikul.odoo.model.customer.signin.SignInForgetPasswordData;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_USERNAME;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class ForgotPasswordDialogFragment extends BaseDialogFragment {

    @SuppressWarnings("unused")
    private static final String TAG = "ForgotPasswordDialogFra";

    public static ForgotPasswordDialogFragment newInstance(String email) {
        ForgotPasswordDialogFragment forgotPasswordDialogFragment = new ForgotPasswordDialogFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_USERNAME, email);
        forgotPasswordDialogFragment.setArguments(args);
        return forgotPasswordDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DialogFragmentForgotPasswordBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_forgot_password, container, false);
        binding.setData(new SignInForgetPasswordData(getArguments().getString(BUNDLE_KEY_USERNAME)));
        binding.setHandler(new ForgotPasswordDialogHandler(getContext(), getDialog(), binding.getData()));
        return binding.getRoot();
    }
}