package com.webkul.mobikul.odoo.model.checkout;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.webkul.mobikul.odoo.BR;

/**
 * Model data related to checkout activity..
 * Primarily used for fragment and backstack management...
 * <p>
 * Created by shubham.agarwal on 11/1/17. @Webkul Software Pvt. Ltd
 */

public class CheckoutActivityData extends BaseObservable {
    private boolean shippingMethodClickable;
    private boolean paymentMethodClickable;
    private boolean orderReviewClickable;

    @Bindable
    public boolean isShippingMethodClickable() {
        return shippingMethodClickable;
    }

    public void setShippingMethodClickable(boolean shippingMethodClickable) {
        this.shippingMethodClickable = shippingMethodClickable;
        notifyPropertyChanged(BR.shippingMethodClickable);
    }

    @Bindable
    public boolean isPaymentMethodClickable() {
        return paymentMethodClickable;
    }

    public void setPaymentMethodClickable(boolean paymentMethodClickable) {
        this.paymentMethodClickable = paymentMethodClickable;
        notifyPropertyChanged(BR.paymentMethodClickable);
    }

    @Bindable
    public boolean isOrderReviewClickable() {
        return orderReviewClickable;
    }

    public void setOrderReviewClickable(boolean orderReviewClickable) {
        this.orderReviewClickable = orderReviewClickable;
        notifyPropertyChanged(BR.orderReviewClickable);
    }
}
