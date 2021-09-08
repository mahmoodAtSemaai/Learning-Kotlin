package com.webkul.mobikul.odoo.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.FragmentCameraSearchChoiceDialogBinding;
import com.webkul.mobikul.odoo.handler.extra.search.CameraSearchHandler;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class CameraSearchChoiceDialogFragment extends DialogFragment {
    private FragmentCameraSearchChoiceDialogBinding mBinding;

    public static CameraSearchChoiceDialogFragment newInstance() {

        Bundle args = new Bundle();

        CameraSearchChoiceDialogFragment fragment = new CameraSearchChoiceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_camera_search_choice_dialog, container, false);

        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setHandler(new CameraSearchHandler(this.getContext(), this));

    }
}
