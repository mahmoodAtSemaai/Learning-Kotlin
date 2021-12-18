package com.webkul.mobikul.odoo.model.customer.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 10/5/17.
 */

public class AddressData implements Parcelable {
    public static final Creator<AddressData> CREATOR = new Creator<AddressData>() {
        @Override
        public AddressData createFromParcel(Parcel in) {
            return new AddressData(in);
        }

        @Override
        public AddressData[] newArray(int size) {
            return new AddressData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "AddressData";
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("addressId")
    @Expose
    private String addressId;
    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected AddressData(Parcel in) {
        url = in.readString();
        displayName = in.readString();
        addressId = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(displayName);
        dest.writeString(addressId);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUrl() {
        if (url == null) {
            return "";
        }

        return url;
    }

    public String getDisplayName() {
        if (displayName == null) {
            return "";
        }

        return displayName;
    }


    public String getAddressId() {
        if (addressId == null) {
            return "";
        }
        return addressId;
    }
}
