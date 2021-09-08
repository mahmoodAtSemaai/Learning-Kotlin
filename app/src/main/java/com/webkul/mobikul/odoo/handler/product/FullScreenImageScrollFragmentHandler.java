package com.webkul.mobikul.odoo.handler.product;

import android.content.Context;

import com.webkul.mobikul.odoo.activity.BaseActivity;

/**
 * Created by shubham.agarwal on 31/5/17.
 */

public class FullScreenImageScrollFragmentHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "FullScreenImageScrollFr";
    private Context mContext;


    public FullScreenImageScrollFragmentHandler(Context context) {
        mContext = context;
    }


    public void closeView() {
        ((BaseActivity) mContext).mSupportFragmentManager.popBackStack();
    }
}
