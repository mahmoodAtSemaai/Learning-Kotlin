package com.webkul.mobikul.odoo.model.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse;
import com.webkul.mobikul.odoo.model.generic.BannerImageData;
import com.webkul.mobikul.odoo.model.generic.CategoryData;
import com.webkul.mobikul.odoo.model.generic.FeaturedCategoryData;
import com.webkul.mobikul.odoo.model.generic.ProductSliderData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shubham.agarwal on 2/5/17.
 */

public class HomePageResponse extends LoginResponse implements Parcelable {

    public static final Creator<HomePageResponse> CREATOR = new Creator<HomePageResponse>() {
        @Override
        public HomePageResponse createFromParcel(Parcel in) {
            return new HomePageResponse(in);
        }

        @Override
        public HomePageResponse[] newArray(int size) {
            return new HomePageResponse[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "HomePageResponse";
    @SerializedName("bannerImages")
    @Expose
    private List<BannerImageData> bannerImages = null;
    @SerializedName("featuredCategories")
    @Expose
    private List<FeaturedCategoryData> featuredCategories = null;
    @SerializedName("productSliders")
    @Expose
    private List<ProductSliderData> productSliders = null;
    @SerializedName("categories")
    @Expose
    private List<CategoryData> categories = null;
    @SerializedName("allLanguages")
    @Expose
    private List<List<String>> allLanguages = new ArrayList<>();

    private HashMap<String, String> languageMap = null;

    public HomePageResponse(Parcel in) {
        super(in);
        if (in == null) {
            return;
        }
        bannerImages = in.createTypedArrayList(BannerImageData.CREATOR);
        featuredCategories = in.createTypedArrayList(FeaturedCategoryData.CREATOR);
        productSliders = in.createTypedArrayList(ProductSliderData.CREATOR);
        categories = in.createTypedArrayList(CategoryData.CREATOR);
        in.readList(this.allLanguages, ArrayList.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(bannerImages);
        dest.writeTypedList(featuredCategories);
        dest.writeTypedList(productSliders);
        dest.writeTypedList(categories);
        dest.writeList(allLanguages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<BannerImageData> getBannerImages() {
        if (bannerImages == null) {
            return new ArrayList<>();
        }
        return bannerImages;
    }


    public List<FeaturedCategoryData> getFeaturedCategories() {
        if (featuredCategories == null) {
            return new ArrayList<>();
        }
        return featuredCategories;
    }

    public List<ProductSliderData> getProductSliders() {
        if (productSliders == null) {
            return new ArrayList<>();
        }
        return productSliders;
    }

    public List<CategoryData> getCategories() {
        if (categories == null) {
            return new ArrayList<>();
        }
        return categories;
    }

    public List<List<String>> getAllLanguages() {
        return allLanguages;
    }

    public HashMap<String, String> getLanguageMap() {
        if (getAllLanguages() == null) {
            return null;
        }
        languageMap = new HashMap<>();
        for (List<String> list : getAllLanguages()) {
            languageMap.put(list.get(0), list.get(1));
        }
        return languageMap;
    }
}
