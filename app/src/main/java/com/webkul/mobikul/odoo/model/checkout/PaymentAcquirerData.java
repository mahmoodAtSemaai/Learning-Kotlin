package com.webkul.mobikul.odoo.model.checkout;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 25/5/17.
 */

public class PaymentAcquirerData implements Parcelable {
    public static final Creator<PaymentAcquirerData> CREATOR = new Creator<PaymentAcquirerData>() {
        @Override
        public PaymentAcquirerData createFromParcel(Parcel in) {
            return new PaymentAcquirerData(in);
        }

        @Override
        public PaymentAcquirerData[] newArray(int size) {
            return new PaymentAcquirerData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "PaymentAcquirerData";
    /*GENERATED IN PAYMENT ACQUIRER*/
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("thumbNail")
    @Expose
    private String thumbNail;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    /*GENERATED IN ORDER REVIEW*/
    @SerializedName("paymentReference")
    @Expose
    private String paymentReference;
    @SerializedName("secret_key")
    @Expose
    private String secretKey;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("publishable_key")
    @Expose
    private String publishableKey;
    @SerializedName("auth")
    @Expose
    private boolean auth;
    @SerializedName("status")
    @Expose
    private boolean status;

    protected PaymentAcquirerData(Parcel in) {
        id = in.readString();
        thumbNail = in.readString();
        name = in.readString();
        description = in.readString();
        paymentReference = in.readString();
        secretKey = in.readString();
        code = in.readString();
        publishableKey = in.readString();
        auth = in.readByte() != 0;
        status = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(thumbNail);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(paymentReference);
        dest.writeString(secretKey);
        dest.writeString(code);
        dest.writeString(publishableKey);
        dest.writeByte((byte) (auth ? 1 : 0));
        dest.writeByte((byte) (status ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public String getThumbNail() {
        if (thumbNail == null) {
            return "";
        }
        return thumbNail;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }


    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    public String getPaymentReference() {
        if (paymentReference == null) {
            return "";
        }
        return paymentReference;
    }

    public String getSecretKey() {
        if (secretKey == null) {
            return "";
        }
        return secretKey;
    }

    public String getCode() {
        if (code == null) {
            return "";
        }
        return code;
    }

    public String getPublishableKey() {
        if (publishableKey == null) {
            return "";
        }
        return publishableKey;
    }

    public boolean isAuth() {
        return auth;
    }

    public boolean isStatus() {
        return status;
    }
}
