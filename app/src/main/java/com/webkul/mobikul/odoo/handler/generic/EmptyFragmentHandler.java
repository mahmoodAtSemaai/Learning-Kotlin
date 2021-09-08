package com.webkul.mobikul.odoo.handler.generic;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.activity.HomeActivity;


/**
 * Created by shubham.agarwal on 24/1/17. @Webkul Software Pvt. Ltd
 */

public class EmptyFragmentHandler {

    private Context mContext;

    public EmptyFragmentHandler(Context context) {
        mContext = context;
    }

    public void continueShopping() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
    }
}
