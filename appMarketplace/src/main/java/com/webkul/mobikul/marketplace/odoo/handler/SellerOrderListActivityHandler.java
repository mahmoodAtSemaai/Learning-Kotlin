package com.webkul.mobikul.marketplace.odoo.handler;

import android.content.Context;

import com.webkul.mobikul.marketplace.odoo.model.SellerOrderLine;

/**
 * Created by aastha.gupta on 1/3/18 in Mobikul_Odoo_Application.
 */

public class SellerOrderListActivityHandler {

    private Context mContext;
    private SellerOrderLine mOrderLine;

    public SellerOrderListActivityHandler(Context mContext, SellerOrderLine orderLine) {
        this.mContext = mContext;
        mOrderLine = orderLine;
    }

    public void onClickOrderLine() {
//        Intent intent = new Intent(mContext, SellerOrderDetailsActivity.class);
//        intent.putExtra(BUNDLE_KEY_ORDER_NAME, mOrderLine.getLineId());
//        mContext.startActivity(intent);
    }
}
