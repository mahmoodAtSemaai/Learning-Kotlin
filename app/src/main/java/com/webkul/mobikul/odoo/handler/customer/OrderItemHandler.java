package com.webkul.mobikul.odoo.handler.customer;

import android.app.Activity;
import android.content.Context;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.fragment.OrderFragment;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.model.customer.order.OrderData;


/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

public class OrderItemHandler {

    private final Context mContext;
    private final OrderData mOrderData;
    private final String mSource;

    public OrderItemHandler(Context context, OrderData orderData, String source) {
        mContext = context;
        mOrderData = orderData;
        mSource = source;
    }

    public void viewOrderDetail() {
        ((Activity) mContext).setTitle(mContext.getString(R.string.my_order));
        AnalyticsImpl.INSTANCE.trackOrderSelected(mOrderData.getId(), mSource, mOrderData.getAmountTotal(), mOrderData.getStatus(), mOrderData.getCreateDate());
        FragmentHelper.replaceFragment(R.id.container, mContext, OrderFragment.newInstance(mOrderData.getUrl()), OrderFragment.class.getSimpleName(), true, false);
    }

}
