package com.webkul.mobikul.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * GENERIC DATA which is used to represent what kind of slider to be displayed dynamically...
 * <p>
 * Created by shubham.agarwal on 3/5/17.
 */

public class ProductSliderData implements Parcelable {

    public static final Creator<ProductSliderData> CREATOR = new Creator<ProductSliderData>() {
        @Override
        public ProductSliderData createFromParcel(Parcel in) {
            return new ProductSliderData(in);
        }

        @Override
        public ProductSliderData[] newArray(int size) {
            return new ProductSliderData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "ProductSliderData";
    @SerializedName("product_img_position")
    @Expose
    private String productImgPosition;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("item_display_limit")
    @Expose
    private int itemDisplayLimit;
    @SerializedName("slider_mode")
    @Expose
    private String sliderMode;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("products")
    @Expose
    private List<ProductData> products = null;

    protected ProductSliderData(Parcel in) {
        productImgPosition = in.readString();
        title = in.readString();
        itemDisplayLimit = in.readInt();
        sliderMode = in.readString();
        url = in.readString();
        products = in.createTypedArrayList(ProductData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productImgPosition);
        dest.writeString(title);
        dest.writeInt(itemDisplayLimit);
        dest.writeString(sliderMode);
        dest.writeString(url);
        dest.writeTypedList(products);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getProductImgPosition() {
        if (productImgPosition == null) {
            return "";
        }
        return productImgPosition;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        }
        return title;
    }

    public int getItemDisplayLimit() {
        return itemDisplayLimit;
    }

    public String getSliderMode() {
        if (sliderMode == null) {
            return "";
        }

        return sliderMode;
    }

    public String getUrl() {
        if (url == null) {
            return "";
        }
        return url;
    }

    public List<ProductData> getProducts() {
        if (products == null) {
            return new ArrayList<>();
        }

        return products;
    }


}
