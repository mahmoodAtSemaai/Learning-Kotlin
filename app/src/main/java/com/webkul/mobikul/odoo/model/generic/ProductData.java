package com.webkul.mobikul.odoo.model.generic;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.QTY_ZERO;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.adapter.product.MobikulCategoryDetails;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.constant.ApplicationConstant;
import com.webkul.mobikul.odoo.model.Seller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 2/5/17.
 */

public class ProductData extends BaseObservable implements Parcelable {

    public static final Creator<ProductData> CREATOR = new Creator<ProductData>() {
        @Override
        public ProductData createFromParcel(Parcel in) {
            return new ProductData(in);
        }

        @Override
        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "ProductData";
    @SerializedName("variants")
    @Expose
    private List<ProductVariant> variants;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("images")
    @Expose
    private List<String> images;
    @SerializedName("productCount")
    @Expose
    private int productCount;
    @SerializedName("priceUnit")
    @Expose
    private String priceUnit;
    @SerializedName("priceReduce")
    @Expose
    private String priceReduce;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("templateId")
    @Expose
    private String templateId;
    @SerializedName("attributes")
    @Expose
    private List<AttributeData> attributes = null;
    @SerializedName("thumbNail")
    @Expose
    private String thumbNail;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("addedToWishlist")
    @Expose
    private boolean addedToWishlist;
    @SerializedName("avg_rating")
    @Expose
    private float productAvgRating;
    @SerializedName("total_review")
    @Expose
    private int totalreviewsAvailable;
    @SerializedName("seller_info")
    @Expose
    private Seller seller;
    @SerializedName("qty")
    @Expose
    private String forecastQuantity;
    @SerializedName("mobikul_category_details")
    @Expose
    private MobikulCategoryDetails mobikulCategoryDetails;
    @SerializedName("brand_name")
    @Expose
    private String brandName;

    private boolean inStock;

    private int quantity = 1;
    @SerializedName("accessDenied")
    @Expose
    private boolean accessDenied;

    @SerializedName("alternativeProducts")
    @Expose
    private ArrayList<ProductData> alternativeProducts;

    @SerializedName("inventory_availability")
    @Expose
    private String inventoryAvailability;

    @SerializedName("available_quantity")
    @Expose
    private int availableQuantity;

    @SerializedName("available_threshold")
    @Expose
    private int availableThreshold;

    public String getAbsoluteUrl() {
        return absoluteUrl;
    }

    public void setAbsoluteUrl(String absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }

    @SerializedName("absoluteUrl")
    @Expose
    private String absoluteUrl;


    protected ProductData(Parcel in) {
        variants = in.createTypedArrayList(ProductVariant.CREATOR);
        name = in.readString();
        image = in.readString();
        images = in.createStringArrayList();
        productCount = in.readInt();
        priceUnit = in.readString();
        priceReduce = in.readString();
        productId = in.readString();
        templateId = in.readString();
        attributes = in.createTypedArrayList(AttributeData.CREATOR);
        thumbNail = in.readString();
        description = in.readString();
        addedToWishlist = in.readByte() != 0;
        productAvgRating = in.readFloat();
        totalreviewsAvailable = in.readInt();
        forecastQuantity = in.readString();
        quantity = in.readInt();
        accessDenied = in.readByte() != 0;
        alternativeProducts = in.createTypedArrayList(ProductData.CREATOR);
        absoluteUrl = in.readString();
        inventoryAvailability = in.readString();
        availableQuantity = in.readInt();
        availableThreshold = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(variants);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeStringList(images);
        dest.writeInt(productCount);
        dest.writeString(priceUnit);
        dest.writeString(priceReduce);
        dest.writeString(productId);
        dest.writeString(templateId);
        dest.writeTypedList(attributes);
        dest.writeString(thumbNail);
        dest.writeString(description);
        dest.writeByte((byte) (addedToWishlist ? 1 : 0));
        dest.writeFloat(productAvgRating);
        dest.writeInt(totalreviewsAvailable);
        dest.writeString(forecastQuantity);
        dest.writeInt(quantity);
        dest.writeByte((byte) (accessDenied ? 1 : 0));
        dest.writeTypedList(alternativeProducts);
        dest.writeString(absoluteUrl);
        dest.writeString(inventoryAvailability);
        dest.writeInt(availableQuantity);
        dest.writeInt(availableThreshold);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public List<ProductVariant> getVariants() {
        if (variants == null) {
            return new ArrayList<>();
        }
        return variants;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MobikulCategoryDetails getMobikulCategoryDetails() {
        return mobikulCategoryDetails;
    }

    public void setMobikulCategoryDetails(MobikulCategoryDetails mobikulCategoryDetails) {
        this.mobikulCategoryDetails = mobikulCategoryDetails;
    }

    public String calculateDiscount() {
        if (!getPriceReduce().isEmpty()) {
            Double original_price = StringtoDouble(priceUnit);
            Double reduced_price = StringtoDouble(priceReduce);
            double discount = (((original_price - reduced_price)) / original_price) * 100;
            discount = (double) (Math.round(discount));
            return "-" + discount + "%";
        } else return "";
    }

    public Double StringtoDouble(String str) {
        String s = str.substring(2);
        String updated_string = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ',') continue;
            updated_string += (s.charAt(i));
        }
        return Double.parseDouble(updated_string);
    }


    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public int getProductCount() {
        return productCount;
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

    public void setPriceReduce(String priceReduce) {
        this.priceReduce = priceReduce;
    }

    public String getProductId() {
        if (productId == null) {
            return "";
        }
        return productId;
    }

    public String getTemplateId() {
        if (templateId == null) {
            return "";
        }

        return templateId;
    }

    public List<AttributeData> getAttributes() {
        if (attributes == null) {
            return new ArrayList<>();
        }

        return attributes;
    }

    public String getThumbNail() {
        if (thumbNail == null) {
            return "";
        }

        return thumbNail;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }

        return description;
    }

    @Bindable
    public boolean isAddedToWishlist() {
//        if(variants != null)
//        {
//            variants
//        }
//        else
//        {
//            return addedToWishlist;
//        }

        return addedToWishlist;
    }

    public void setAddedToWishlist(boolean addedToWishlist) {
        this.addedToWishlist = addedToWishlist;
    }

    public float getProductAvgRating() {
        return productAvgRating;
    }


    public int getTotalreviewsAvailable() {
        return totalreviewsAvailable;
    }

    /*QTY*/
    @Bindable
    public int getQuantity() {
        if (quantity < QTY_ZERO) {
            return 0;
        } else if (quantity == QTY_ZERO) {
            setQuantity(quantity);
        }
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < QTY_ZERO) {
            return;
        } else if ((quantity == QTY_ZERO) && (!isInStock())) {
            this.quantity = QTY_ZERO;
        } else if ((quantity == QTY_ZERO) && (isInStock())) {
            this.quantity = 1;
        } else {
            this.quantity = quantity;
        }
        AnalyticsImpl.INSTANCE.trackItemQuantitySelected(quantity, productId, name);
        notifyPropertyChanged(BR.quantity);
    }

    public List<ProductCombination> getDefaultCombination() {
        for (ProductVariant eachProductVariant : getVariants()) {
            if (eachProductVariant.getProductId().equals(getProductId())) {
                return eachProductVariant.getCombinations();
            }
        }
        return getVariants().get(0).getCombinations();
    }

    public boolean isUnderDefaultCombination(String attributeId, String valueId) {
        if (getVariants().size() > 0) {
            for (ProductCombination eachProductCombination : getDefaultCombination()) {
                if (eachProductCombination.getAttributeId().equals(attributeId) && eachProductCombination.getValueId().equals(valueId)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public Seller getSeller() {
        return seller;
    }

    public String getForecastQuantity() {
        return forecastQuantity;
    }

    public boolean isInStock() {
        return getAvailableQuantity() > 0;
    }

    public boolean isThreshold() {
        return inventoryAvailability.equals(ApplicationConstant.THRESHOLD);
    }

    public boolean isAlways() {
        return inventoryAvailability.equals(ApplicationConstant.ALWAYS);
    }


    public boolean isNever() {
        return inventoryAvailability.equals(ApplicationConstant.NEVER);
    }

    public boolean isAccessDenied() {
        return accessDenied;
    }

    public boolean isOutOfStock() {
        return ((isAlways() || isThreshold()) && getQuantity() == 0);
    }


    public ArrayList<ProductData> getAlternativeProducts() {
        if (alternativeProducts == null)
            return new ArrayList<>();
        return alternativeProducts;
    }

    public void setAlternativeProducts(ArrayList<ProductData> alternativeProducts) {
        this.alternativeProducts = alternativeProducts;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getInventoryAvailability() {
        return inventoryAvailability;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public int getAvailableThreshold() {
        return availableThreshold;
    }

    public String calculateProductDetailDiscount() {
        if (!getPriceReduce().isEmpty()) {
            Double original_price = StringtoDouble(priceUnit);
            Double reduced_price = StringtoDouble(priceReduce);
            double discount = (((original_price - reduced_price)) / original_price) * 100;
            discount = (double) (Math.round(discount));
            int discountInInt = (int) discount;
            return discountInInt + "%";
        } else return "";
    }
}
