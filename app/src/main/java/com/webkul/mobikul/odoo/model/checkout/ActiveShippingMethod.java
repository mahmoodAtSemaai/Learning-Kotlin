package com.webkul.mobikul.odoo.model.checkout;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aastha.gupta on 19/3/18 in Mobikul_Odoo_Application.
 */

public class ActiveShippingMethod implements Parcelable {

    public static final Creator<ActiveShippingMethod> CREATOR = new Creator<ActiveShippingMethod>() {
        @Override
        public ActiveShippingMethod createFromParcel(Parcel in) {
            return new ActiveShippingMethod(in);
        }

        @Override
        public ActiveShippingMethod[] newArray(int size) {
            return new ActiveShippingMethod[size];
        }
    };
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private int id;

    protected ActiveShippingMethod(Parcel in) {
        price = in.readString();
        description = in.readString();
        name = in.readString();
        id = in.readInt();
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(price);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeInt(id);
    }
}
