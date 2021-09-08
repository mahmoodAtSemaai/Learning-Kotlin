package com.webkul.mobikul.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 22/5/17.
 */

public class ProductCombination implements Parcelable {
    public static final Creator<ProductCombination> CREATOR = new Creator<ProductCombination>() {
        @Override
        public ProductCombination createFromParcel(Parcel in) {
            return new ProductCombination(in);
        }

        @Override
        public ProductCombination[] newArray(int size) {
            return new ProductCombination[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "ProductCombination";
    @SerializedName("valueId")
    @Expose
    private String valueId;
    @SerializedName("attributeId")
    @Expose
    private String attributeId;

    private ProductCombination(Parcel in) {
        valueId = in.readString();
        attributeId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(valueId);
        dest.writeString(attributeId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("WeakerAccess")
    public String getValueId() {
        if (valueId == null) {
            return "";
        }
        return valueId;
    }

    @SuppressWarnings("WeakerAccess")
    public String getAttributeId() {
        if (attributeId == null) {
            return "";
        }
        return attributeId;
    }
}
