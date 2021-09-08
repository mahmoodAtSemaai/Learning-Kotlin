package com.webkul.mobikul.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 22/5/17.
 */

public class ProductVariant implements Parcelable {

    public static final Creator<ProductVariant> CREATOR = new Creator<ProductVariant>() {
        @Override
        public ProductVariant createFromParcel(Parcel in) {
            return new ProductVariant(in);
        }

        @Override
        public ProductVariant[] newArray(int size) {
            return new ProductVariant[size];
        }
    };
    @SerializedName("combinations")
    @Expose
    private List<ProductCombination> combinations = null;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("images")
    @Expose
    private List<String> images;
    @SerializedName("priceUnit")
    @Expose
    private String priceUnit;
    @SerializedName("priceReduce")
    @Expose
    private String priceReduce;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("addedToWishlist")
    @Expose
    private boolean addedToWishlist;

    protected ProductVariant(Parcel in) {
        combinations = in.createTypedArrayList(ProductCombination.CREATOR);
        image = in.readString();
        images = in.createStringArrayList();
        priceUnit = in.readString();
        priceReduce = in.readString();
        productId = in.readString();
        addedToWishlist = in.readByte() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(combinations);
        dest.writeString(image);
        dest.writeStringList(images);
        dest.writeString(priceUnit);
        dest.writeString(priceReduce);
        dest.writeString(productId);
        dest.writeByte((byte) (addedToWishlist ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<ProductCombination> getCombinations() {
        if (combinations == null) {
            return new ArrayList<>();
        }
        return combinations;
    }

    public String getImage() {
        if (image == null) {
            return "";
        }
        return image;
    }

    public List<String> getImages() {
        if (images == null) {
            return new ArrayList<>();
        }
        return images;
    }

    public String getPriceUnit() {
        if (priceUnit == null) {
            return "";
        }
        return priceUnit;
    }

    public String getPriceReduce() {
        if (priceReduce == null) {
            return "";
        }
        return priceReduce;
    }

    public String getProductId() {
        if (productId == null) {
            return "";
        }
        return productId;
    }

    public boolean isAddedToWishlist() {
        return addedToWishlist;
    }
}
