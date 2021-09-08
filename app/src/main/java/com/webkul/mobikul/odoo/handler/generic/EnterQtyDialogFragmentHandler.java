package com.webkul.mobikul.odoo.handler.generic;

import android.content.Context;
import androidx.fragment.app.Fragment;

import com.webkul.mobikul.odoo.activity.BagActivity;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.ProductActivity;
import com.webkul.mobikul.odoo.dialog_frag.EnterQtyDialogFragment;
import com.webkul.mobikul.odoo.model.generic.EnterQtyDialogFragmentData;

/**
 * Created by shubham.agarwal on 6/6/17.
 */

public class EnterQtyDialogFragmentHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "EnterQtyDialogFragmentH";
    private Context mContext;
    private EnterQtyDialogFragmentData mData;


    public EnterQtyDialogFragmentHandler(Context context, EnterQtyDialogFragmentData data) {
        mContext = context;
        mData = data;
    }


    public void dismissDialog() {
        Fragment fragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(EnterQtyDialogFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            ((EnterQtyDialogFragment) fragment).dismiss();
        }
    }

    public void saveQty() {
         /*dismissing the dialog first so that content disposable is not called after that..*/
        if (mData.isFormValidated()) {
            dismissDialog();
            if (mContext instanceof BagActivity) {
                EnterQtyDialogFragment.sOnQtyChangeListener.onQtyChanged(Integer.parseInt(mData.getQty()));
            } else if (mContext instanceof ProductActivity) {
                ((ProductActivity) mContext).mBinding.getHandler().onQtyChanged(Integer.parseInt(mData.getQty()));
            }
        }
    }
}
