package com.webkul.mobikul.odoo.ui.checkout.address;


import android.content.Context;
import com.webkul.mobikul.odoo.model.customer.address.AddressData;


public class CheckoutAddressRecyclerViewHandlerV1 {

    private final Context context;
    private final OnAddressClickListener addressClickListener;
    private AddressData data;

    public CheckoutAddressRecyclerViewHandlerV1(Context context, AddressData additionalAddress, OnAddressClickListener onAdditionalAddressDeletedListener) {
        this.context = context;
        data = additionalAddress;
        addressClickListener = onAdditionalAddressDeletedListener;
    }

    public void editAddress() {
        addressClickListener.onAddressEditClick(data);
    }

    public void deleteAddress() {
        addressClickListener.onAddressDeleteClick(data);
    }

    public interface OnAddressClickListener {

        void onAddressDeleteClick(AddressData addressData);

        void onAddressEditClick(AddressData addressData);
    }
}
