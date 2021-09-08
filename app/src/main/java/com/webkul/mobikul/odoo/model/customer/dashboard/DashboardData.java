package com.webkul.mobikul.odoo.model.customer.dashboard;

import android.content.Context;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressData;
import com.webkul.mobikul.odoo.model.customer.order.OrderData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 3/4/17.
 */

public class DashboardData extends BaseResponse {

    @SuppressWarnings("unused")
    private static final String TAG = "DashboardData";

    private List<OrderData> recentOrders;

    private List<AddressData> addresses;
    private AddressData defaultShippingAddress;
    private final Context mContext;

    public DashboardData(Context context) {
        super(null);
        mContext = context;
    }

    public List<OrderData> getRecentOrders() {
        if (recentOrders == null) {
            return new ArrayList<>();
        }
        return recentOrders;
    }

    public void setRecentOrders(List<OrderData> recentOrders) {
        this.recentOrders = recentOrders;
    }

    public List<AddressData> getAddresses() {
        if (addresses == null) {
            return new ArrayList<>();
        }
        return addresses;
    }

    public void setAddresses(List<AddressData> addresses) {
        this.addresses = addresses;
    }

    public String getBillingAddress() {
        try {
            if (getAddresses().get(0).getDisplayName().replaceAll("\\n", "").trim().isEmpty()) {
                return mContext.getString(R.string.error_billing_address_not_configured);
            }
            return getAddresses().get(0).getDisplayName();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public String getShippingAddress() {
        try {
            if (defaultShippingAddress == null) {
                return getAddresses().get(1).getDisplayName();
            }
            return defaultShippingAddress.getDisplayName().trim();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public void setDefaultShippingAddress(AddressData address) {
        defaultShippingAddress = address;
    }

    public void updateDefaultShippingAddress(String addressId) {
        for (AddressData address : getAddresses()) {
            if (address.getAddressId().equalsIgnoreCase(addressId)) {
                setDefaultShippingAddress(address);
                notifyChange();
                break;
            }
        }
    }
}
