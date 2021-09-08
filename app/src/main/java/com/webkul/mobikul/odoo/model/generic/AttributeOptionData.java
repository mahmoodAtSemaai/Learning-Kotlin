package com.webkul.mobikul.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 22/5/17.
 */

public class AttributeOptionData implements Parcelable {

    public static final Creator<AttributeOptionData> CREATOR = new Creator<AttributeOptionData>() {
        @Override
        public AttributeOptionData createFromParcel(Parcel in) {
            return new AttributeOptionData(in);
        }

        @Override
        public AttributeOptionData[] newArray(int size) {
            return new AttributeOptionData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "AttributeOptionData";
    @SerializedName("valueId")
    @Expose
    private String valueId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price_extra")
    @Expose
    private int priceExtra;
    @SerializedName("htmlCode")
    @Expose
    private String htmlCode;

    protected AttributeOptionData(Parcel in) {
        valueId = in.readString();
        name = in.readString();
        priceExtra = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(valueId);
        dest.writeString(name);
        dest.writeInt(priceExtra);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getValueId() {
        if (valueId == null) {
            return "";
        }
        return valueId;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }

    public int getPriceExtra() {
        return priceExtra;
    }

    public String getHtmlCode() {
        if (htmlCode == null) {
            return "";
        }
        return htmlCode;
    }
}
