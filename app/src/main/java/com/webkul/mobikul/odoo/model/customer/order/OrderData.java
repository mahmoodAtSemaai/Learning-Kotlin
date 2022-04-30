package com.webkul.mobikul.odoo.model.customer.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 10/5/17.
 */

public class OrderData implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("canReorder")
    @Expose
    private boolean canReorder;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("shipAdd_url")
    @Expose
    private String shipAddUrl;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount_total")
    @Expose
    private String amountTotal;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;

    @SerializedName("display_order_status")
    @Expose
    private String mobileOrderStatus;


    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;

    protected OrderData(Parcel in) {
        status = in.readString();
        canReorder = in.readByte() != 0;
        createDate = in.readString();
        name = in.readString();
        mobileOrderStatus = in.readString();
        shippingAddress = in.readString();
        url = in.readString();
        shipAddUrl = in.readString();
        id = in.readString();
        amountTotal = in.readString();
        paymentMode = in.readString();
        paymentStatus = in.readString();
    }

    public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel in) {
            return new OrderData(in);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };

    public String getStatus() {
        if (status == null) {
            return "";
        }
        return status;
    }

    public boolean isCanReorder() {
        return canReorder;
    }

    public String getCreateDate() {
        if (createDate == null) {
            return "";
        }

        return createDate;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }

    public String getShippingAddress() {
        if (shippingAddress == null) {
            return "";
        }

        return shippingAddress;
    }

    public String getUrl() {
        if (url == null) {
            return "";
        }

        return url;
    }

    public String getShipAddUrl() {
        if (shipAddUrl == null) {
            return "";
        }

        return shipAddUrl;
    }

    public String getId() {
        if (id == null) {
            return "";
        }

        return id;
    }

    public String getAmountTotal() {
        if (amountTotal == null) {
            return "";
        }

        return amountTotal;
    }

    public String getPaymentMode() {
        if (paymentMode == null)
            return "";
        return paymentMode;
    }

    public String getPaymentStatus() {
        if (paymentStatus == null)
            return "";
        return paymentStatus;
    }

    public String getMobileOrderStatus() {
        return mobileOrderStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeByte((byte) (canReorder ? 1 : 0));
        parcel.writeString(createDate);
        parcel.writeString(name);
        parcel.writeString(shippingAddress);
        parcel.writeString(mobileOrderStatus);
        parcel.writeString(url);
        parcel.writeString(shipAddUrl);
        parcel.writeString(id);
        parcel.writeString(amountTotal);
        parcel.writeString(paymentMode);
        parcel.writeString(paymentStatus);
    }
}
