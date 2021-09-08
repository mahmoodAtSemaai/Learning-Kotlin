package com.webkul.mobikul.odoo.model.customer.signin;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 11/4/17.
 */

public class FacebookPictureData implements Parcelable {
    public static final Creator<FacebookPictureData> CREATOR = new Creator<FacebookPictureData>() {
        @Override
        public FacebookPictureData createFromParcel(Parcel in) {
            return new FacebookPictureData(in);
        }

        @Override
        public FacebookPictureData[] newArray(int size) {
            return new FacebookPictureData[size];
        }
    };
    @SerializedName("data")
    @Expose
    private FacebookData data;

    FacebookPictureData(Parcel in) {
        if (in == null) {
            data = new FacebookData(null);
        } else {
            data = in.readParcelable(FacebookData.class.getClassLoader());
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public FacebookData getData() {
        if (data == null) {
            return new FacebookData(null);
        }
        return data;
    }


}
