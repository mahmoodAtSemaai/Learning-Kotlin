package com.webkul.mobikul.odoo.handler.customer;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

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

    private final Context context;
    private final OrderData orderData;
    private final String source;

    public OrderItemHandler(Context context, OrderData orderData, String source) {
        this.context = context;
        this.orderData = orderData;
        this.source = source;
    }

    public void viewOrderDetail() {
        ((Activity) context).setTitle(context.getString(R.string.my_order));
        AnalyticsImpl.INSTANCE.trackOrderSelected(orderData.getId(), source, orderData.getAmountTotal(), orderData.getStatus(), orderData.getCreateDate());
        FragmentHelper.replaceFragment(R.id.container, context, OrderFragment.newInstance(orderData.getId(), ""), OrderFragment.class.getSimpleName(), true, false);
    }

}
