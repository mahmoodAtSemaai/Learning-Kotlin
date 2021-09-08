package com.webkul.mobikul.odoo.handler.generic;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.dialog_frag.ChangeQtyDialogFragment;
import com.webkul.mobikul.odoo.dialog_frag.EnterQtyDialogFragment;

import static com.webkul.mobikul.odoo.BuildConfig.DEFAULT_MAX_QTY;

/**
 * Created by shubham.agarwal on 23/5/17.
 */

public class QtyAdapterHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "QtyAdapterHandler";
    private final ChangeQtyDialogFragment.OnQtyChangeListener mOnQtyChangeListener;
    private final Context mContext;
    private int mPosition;
    private int mCurrentQty;

    public QtyAdapterHandler(Context context, int position, int currentQty, ChangeQtyDialogFragment.OnQtyChangeListener onQtyChangeListener) {
        mContext = context;
        this.mPosition = position;
        mCurrentQty = currentQty;
        mOnQtyChangeListener = onQtyChangeListener;
    }

    public void onItemSelected() {
        Fragment fragment = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag(ChangeQtyDialogFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            ((ChangeQtyDialogFragment) fragment).dismiss();
        }

        /*dismissing the dialog first so that content disposable is not called after that..*/
        if (mOnQtyChangeListener != null) {
            if (mPosition == DEFAULT_MAX_QTY) {
                EnterQtyDialogFragment.newInstance(mOnQtyChangeListener, mCurrentQty).show(((BaseActivity) mContext).mSupportFragmentManager, EnterQtyDialogFragment.class.getSimpleName());
            } else {
                mOnQtyChangeListener.onQtyChanged(mPosition + 1);
            }
        }
    }
}
