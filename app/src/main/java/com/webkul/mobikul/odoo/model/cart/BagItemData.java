package com.webkul.mobikul.odoo.model.cart;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.constant.ApplicationConstant;

/**
 * Created by shubham.agarwal on 23/5/17.
 */

public class BagItemData extends BaseObservable implements Parcelable {
    public static final Creator<BagItemData> CREATOR = new Creator<BagItemData>() {
        @Override
        public BagItemData createFromParcel(Parcel in) {
            return new BagItemData(in);
        }

        @Override
        public BagItemData[] newArray(int size) {
            return new BagItemData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "BagItemData";
    /* priceUnit = unit price */
    @SerializedName("priceUnit")
    @Expose
    private String priceUnit;
    @SerializedName("priceReduce")
    @Expose
    private String priceReduce;
    @SerializedName("discount")
    @Expose
    private String discount;
    /*total = total mt after tax/discount nd everything*/
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("lineId")
    @Expose
    private String lineId;
    /*QTY WILL ALWAYS BE INT*/
    @SerializedName("qty")
    @Expose
    private int qty;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("thumbNail")
    @Expose
    private String thumbNail;
    @SerializedName("templateId")
    @Expose
    private String templateId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("available_quantity")
    @Expose
    private int availableQuantity;
    @SerializedName("inventory_availability")
    @Expose
    private String inventoryAvailability;

    protected BagItemData(Parcel in) {
        priceUnit = in.readString();
        priceReduce = in.readString();
        discount = in.readString();
        total = in.readString();
        lineId = in.readString();
        qty = in.readInt();
        name = in.readString();
        productName = in.readString();
        thumbNail = in.readString();
        templateId = in.readString();
        productId = in.readString();
        message = in.readString();
        availableQuantity = in.readInt();
        inventoryAvailability = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(priceUnit);
        dest.writeString(priceReduce);
        dest.writeString(discount);
        dest.writeString(total);
        dest.writeString(lineId);
        dest.writeInt(qty);
        dest.writeString(name);
        dest.writeString(productName);
        dest.writeString(thumbNail);
        dest.writeString(templateId);
        dest.writeString(productId);
        dest.writeString(message);
        dest.writeInt(availableQuantity);
        dest.writeString(inventoryAvailability);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPriceUnit() {
        if (priceUnit == null) {
            return "";
        }
        return priceUnit;
    }

    public String getPriceReduce() {
        return priceReduce;
    }

    public String getDiscount() {
        return discount;
    }

    public String getTotal() {
        if (total == null) {
            return "";
        }
        return total;
    }

    public String getLineId() {
        if (lineId == null) {
            return "";
        }
        return lineId;
    }

    /*QTY*/
    @Bindable
    public int getQty() {
        if (qty < 1) {
            return 1;
        }
        return qty;
    }

    public void setQty(int qty) {
        if (qty < 1) {
            return;
        }
        this.qty = qty;
        notifyPropertyChanged(BR.qty);
    }


    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public String getProductName() {
        if (productName == null || productName.isEmpty()) {
            return getName();
        }
        return productName;
    }

    public String getThumbNail() {
        if (thumbNail == null) {
            return "";
        }
        return thumbNail;
    }

    public String getTemplateId() {
        if (templateId == null) {
            return "";
        }
        return templateId;
    }

    public String getProductId() {
        if (productId == null) {
            return "";
        }
        return productId;
    }

    public String getMessage() {
        return message;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public boolean isNever() {
        return inventoryAvailability.equals(ApplicationConstant.NEVER);
    }
    public boolean isThreshold() {
        return inventoryAvailability.equals(ApplicationConstant.THRESHOLD);
    }
    public boolean isAlways() {
        return inventoryAvailability.equals(ApplicationConstant.ALWAYS);
    }

}
