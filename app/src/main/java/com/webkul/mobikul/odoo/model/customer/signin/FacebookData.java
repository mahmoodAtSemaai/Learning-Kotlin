package com.webkul.mobikul.odoo.model.customer.signin;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 11/4/17.
 */

@SuppressWarnings("WeakerAccess")
public class FacebookData implements Parcelable {

    public static final Creator<FacebookData> CREATOR = new Creator<FacebookData>() {
        @Override
        public FacebookData createFromParcel(Parcel in) {
            return new FacebookData(in);
        }

        @Override
        public FacebookData[] newArray(int size) {
            return new FacebookData[size];
        }
    };
    @SerializedName("is_silhouette")
    @Expose
    private boolean isSilhouette;
    @SerializedName("url")
    @Expose
    private String url;

    protected FacebookData(Parcel in) {
        if (in == null) {
            return;
        }

        isSilhouette = in.readByte() != 0;
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSilhouette ? 1 : 0));
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isIsSilhouette() {
        return isSilhouette;
    }

    public String getUrl() {
        if (url == null) {
            return "";
        }
        return url;
    }
}
