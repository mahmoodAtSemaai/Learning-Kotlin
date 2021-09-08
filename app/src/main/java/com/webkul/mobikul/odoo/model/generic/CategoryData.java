package com.webkul.mobikul.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 2/5/17.
 */
public class CategoryData implements Parcelable, Parent<CategoryData> {
    public static final Creator<CategoryData> CREATOR = new Creator<CategoryData>() {
        @Override
        public CategoryData createFromParcel(Parcel in) {
            return new CategoryData(in);
        }

        @Override
        public CategoryData[] newArray(int size) {
            return new CategoryData[size];
        }
    };

    @SuppressWarnings("unused")
    private static final String TAG = "CategoryData";
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("children")
    @Expose
    private List<CategoryData> children = null;

    protected CategoryData(Parcel in) {
        name = in.readString();
        categoryId = in.readString();
        icon = in.readString();
        banner = in.readString();
        children = in.createTypedArrayList(CategoryData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(categoryId);
        dest.writeString(icon);
        dest.writeString(banner);
        dest.writeTypedList(children);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public String getCategoryId() {
        if (categoryId == null) {
            return "";
        }
        return categoryId;
    }


    public String getIcon() {
        if (icon == null) {
            return "";
        }
        return icon;
    }

    @SuppressWarnings("unused")
    public String getBanner() {
        if (banner == null) {
            return "";
        }
        return banner;
    }

    public List<CategoryData> getChildren() {
        if (children == null) {
            return new ArrayList<>();
        }
        return children;
    }

    @Override
    public List<CategoryData> getChildList() {
        return getChildren();
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
