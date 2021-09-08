package com.webkul.mobikul.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 2/5/17.
 */

public class FeaturedCategoryData implements Parcelable {

    public static final Creator<FeaturedCategoryData> CREATOR = new Creator<FeaturedCategoryData>() {
        @Override
        public FeaturedCategoryData createFromParcel(Parcel in) {
            return new FeaturedCategoryData(in);
        }

        @Override
        public FeaturedCategoryData[] newArray(int size) {
            return new FeaturedCategoryData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "FeaturedCategoryData";
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;

    protected FeaturedCategoryData(Parcel in) {
        url = in.readString();
        categoryName = in.readString();
        categoryId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(categoryName);
        dest.writeString(categoryId);
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

    public String getCategoryName() {
        if (categoryName == null) {
            return "";
        }
        return categoryName;
    }

    public String getCategoryId() {
        if (categoryId == null) {
            return "";
        }
        return categoryId;
    }

}
