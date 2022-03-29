package com.webkul.mobikul.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 2/5/17.
 */

public class BannerImageData implements Parcelable {
    public static final Creator<BannerImageData> CREATOR = new Creator<BannerImageData>() {
        @Override
        public BannerImageData createFromParcel(Parcel in) {
            return new BannerImageData(in);
        }

        @Override
        public BannerImageData[] newArray(int size) {
            return new BannerImageData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "BannerImageData";
    @SerializedName("bannerType")
    @Expose
    private String bannerType;
    /*todo where is url used .. */
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("bannerName")
    @Expose
    private String bannerName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("domain")
    @Expose
    private String domain;

    protected BannerImageData(Parcel in) {
        bannerType = in.readString();
        url = in.readString();
        bannerName = in.readString();
        productId = in.readString();
        id = in.readString();
        domain = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bannerType);
        dest.writeString(url);
        dest.writeString(bannerName);
        dest.writeString(productId);
        dest.writeString(id);
        dest.writeString(domain);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getBannerType() {
        if (bannerType == null) {
            return "";
        }
        return bannerType;
    }

    public String getUrl() {
        if (url == null) {
            return "";
        }
        return url;
    }


    @SuppressWarnings("unused")
    public String getBannerName() {
        if (bannerName == null) {
            return "";
        }
        return bannerName;
    }

    public String getId() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public String getProductId() {
        if (productId == null) {
            return "";
        }
        return productId;
    }

    public String getDomain() {
        if (domain == null) {
            return "";
        }
        return domain;
    }
}
