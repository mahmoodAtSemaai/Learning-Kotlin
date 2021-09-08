package com.webkul.mobikul.odoo.handler.extra.search;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.custom.MaterialSearchView;
import com.webkul.mobikul.odoo.fragment.CameraSearchChoiceDialogFragment;
import com.webkul.mobikul.odoo.helper.Helper;



/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class MaterialSearchViewHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "MaterialSearchViewHandl";

    private MaterialSearchView mMaterialSearchView;

    public MaterialSearchViewHandler(MaterialSearchView materialSearchView) {
        mMaterialSearchView = materialSearchView;
    }

    public void backPressed() {
        Helper.hideKeyboard((Activity) mMaterialSearchView.getContext());
        if (mMaterialSearchView.getContext() instanceof HomeActivity) {
            ((HomeActivity) mMaterialSearchView.getContext()).onBackPressed();
        } else {
            mMaterialSearchView.closeSearch();
        }
    }

    public void  onImageSearchClicked() {
        CameraSearchChoiceDialogFragment cameraSearchChoiceDialogFragment = CameraSearchChoiceDialogFragment.newInstance();
        cameraSearchChoiceDialogFragment.show(((AppCompatActivity)mMaterialSearchView.getmContext()).getSupportFragmentManager(), CameraSearchChoiceDialogFragment.class.getSimpleName());
    }

    public void onVoiceClicked() {
        mMaterialSearchView.onVoiceClicked();
    }

    public void clearSearch() {
        mMaterialSearchView.mBinding.etSearch.setText("");
    }


}
