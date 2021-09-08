package com.webkul.mobikul.odoo.dialog_frag;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.FragmentProfilePictureDialogBinding;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class ProfilePictureDialogFragment extends BaseDialogFragment {

    ImageView iv;
    FragmentProfilePictureDialogBinding mBinding;

    public ProfilePictureDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_picture_dialog, container, false);

//        iv = v.findViewById(R.id.profile_picture);
//        ImageHelper.load(iv, AppSharedPref.getCustomerProfileImage(getContext()), R.drawable.com_facebook_profile_picture_blank_portrait, DiskCacheStrategy.NONE, true, null);

        return mBinding.getRoot();
    }

}
