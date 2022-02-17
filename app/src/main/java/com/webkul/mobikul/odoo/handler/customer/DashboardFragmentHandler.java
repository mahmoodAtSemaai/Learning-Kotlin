package com.webkul.mobikul.odoo.handler.customer;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.dialog_frag.ChangeDefaultShippingDialogFrag;
import com.webkul.mobikul.odoo.fragment.AccountInfoFragment;
import com.webkul.mobikul.odoo.fragment.AddressBookFragment;
import com.webkul.mobikul.odoo.fragment.NewAddressFragment;
import com.webkul.mobikul.odoo.fragment.OrderListFragment;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.model.customer.dashboard.DashboardData;


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

public class DashboardFragmentHandler {
    private final Context mContext;
    private final DashboardData mData;

    public DashboardFragmentHandler(Context context, DashboardData data) {
        mContext = context;
        mData = data;
    }

    public void viewAllOrder() {
        FragmentHelper.replaceFragment(R.id.container, mContext, OrderListFragment.newInstance(), OrderListFragment.class.getSimpleName(), true, true);
    }


    public void editAccountInfo() {
        FragmentHelper.replaceFragment(R.id.container, mContext, AccountInfoFragment.newInstance(), AccountInfoFragment.class.getSimpleName(), true, true);
    }

    public void editBillingAddress() {
        FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(String.valueOf(mData.getAddresses().get(0).getUrl()), mContext.getString(R.string.edit_billing_address)
                , NewAddressFragment.AddressType.TYPE_BILLING), NewAddressFragment.class.getSimpleName(), true, false);
    }

    public void editShippingAddress() {
        FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(String.valueOf(mData.getAddresses().get(1).getUrl()), mContext.getString(R.string.edit_shipping_address)
                , NewAddressFragment.AddressType.TYPE_SHIPPING), NewAddressFragment.class.getSimpleName(), true, false);
    }

    public void changeShippingAddress() {
        FragmentManager supportFragmentManager = ((BaseActivity) mContext).getSupportFragmentManager();
        ChangeDefaultShippingDialogFrag changeDefaultShippingDialogFrag = ChangeDefaultShippingDialogFrag.newInstance(mData);
        changeDefaultShippingDialogFrag.show(supportFragmentManager, ChangeDefaultShippingDialogFrag.class.getSimpleName());
    }


    public void viewAddressBook() {
        FragmentHelper.replaceFragment(R.id.container, mContext, AddressBookFragment.newInstance(), AddressBookFragment.class.getSimpleName(), true, true);
    }


}
