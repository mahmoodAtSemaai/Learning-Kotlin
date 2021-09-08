package com.webkul.mobikul.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 28/3/17.
 */

public class KeyValuePairData implements Parcelable {
    public static final Creator<KeyValuePairData> CREATOR = new Creator<KeyValuePairData>() {
        @Override
        public KeyValuePairData createFromParcel(Parcel in) {
            return new KeyValuePairData(in);
        }

        @Override
        public KeyValuePairData[] newArray(int size) {
            return new KeyValuePairData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "KeyValuePairData";
    @SerializedName(value = "title", alternate = {"label"})
    @Expose
    private String title;
    @SerializedName("value")
    @Expose
    private String value;

    public KeyValuePairData(Parcel in) {
        if (in == null) {
            return;
        }
        title = in.readString();
        value = in.readString();
    }

    public String getTitle() {
        if (title == null) {
            return "";
        }
        return title;
    }

    public String getValue() {
        if (value == null) {
            return "";
        }
        return value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(value);
    }
}
