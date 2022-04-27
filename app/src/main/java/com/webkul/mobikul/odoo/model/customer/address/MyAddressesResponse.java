package com.webkul.mobikul.odoo.model.customer.address;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 10/5/17.
 */

public class MyAddressesResponse extends BaseResponse implements Parcelable {

    public static final Creator<MyAddressesResponse> CREATOR = new Creator<MyAddressesResponse>() {
        @Override
        public MyAddressesResponse createFromParcel(Parcel in) {
            return new MyAddressesResponse(in);
        }

        @Override
        public MyAddressesResponse[] newArray(int size) {
            return new MyAddressesResponse[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "MyAddressesResponse";
    @SerializedName("addresses")
    @Expose
    private List<AddressData> addresses = null;
    @SerializedName("tcount")
    @Expose
    private int totalCount;
    @SerializedName("default_shipping_address")
    @Expose
    private AddressData defaultShippingAddress;
    private Context context;

    private MyAddressesResponse(Parcel in) {
        super(in);
        totalCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<AddressData> getAddresses() {
        if (addresses == null) {
            return new ArrayList<>();
        }

        return addresses;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<AddressData> getAdditionalAddress() {
        return getTotalCount() > 1 ? getAddresses().subList(1, getAddresses().size()) : new ArrayList<>();
    }

    public List<String> getAddressesNames() {
        List<String> addresses = new ArrayList<>();
        for (AddressData eachAddressData : getAddresses()) {
            addresses.add(eachAddressData.getDisplayName());
        }
        return addresses;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public String getBillingAddress() {
        try {
            if (getAddresses().get(0).getDisplayName().replaceAll("\\n", "").trim().isEmpty()) {
                return context.getString(R.string.error_billing_address_not_configured);
            }
            return getAddresses().get(0).getDisplayName();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public AddressData getDefaultShippingAddress() {
        return defaultShippingAddress;
    }

}
