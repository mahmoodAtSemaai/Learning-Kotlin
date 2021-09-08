package com.webkul.mobikul.odoo.handler.checkout;

import android.content.Context;

import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.helper.AppSharedPref;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class CheckoutActivityHandler {

    private Context mContext;

    public CheckoutActivityHandler(Context context) {
        mContext = context;
    }

    public void viewBillingInfo() {
        while (((BaseActivity) mContext).mSupportFragmentManager.getBackStackEntryCount() != 1) {
            ((BaseActivity) mContext).mSupportFragmentManager.popBackStackImmediate();
        }
    }

    public void viewActiveShippingMethodInfo() {
        while (((BaseActivity) mContext).mSupportFragmentManager.getBackStackEntryCount() != 2) {
            ((BaseActivity) mContext).mSupportFragmentManager.popBackStackImmediate();
        }
    }

    public void viewPaymentMethodInfo() {
        if (AppSharedPref.isAllowShipping(mContext)) {
            while (((BaseActivity) mContext).mSupportFragmentManager.getBackStackEntryCount() != 3) {
                ((BaseActivity) mContext).mSupportFragmentManager.popBackStackImmediate();
            }
        } else {
            while (((BaseActivity) mContext).mSupportFragmentManager.getBackStackEntryCount() != 2) {
                ((BaseActivity) mContext).mSupportFragmentManager.popBackStackImmediate();
            }
        }
    }
}
