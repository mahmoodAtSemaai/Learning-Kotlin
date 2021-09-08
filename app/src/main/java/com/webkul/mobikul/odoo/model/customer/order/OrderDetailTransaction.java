package com.webkul.mobikul.odoo.model.customer.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Webkul Software.
 * <p>
 * Android Studio version 3.0+
 * Java version 1.7+
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul.odoo.model.customer.order
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class OrderDetailTransaction implements Parcelable {

    @SerializedName("stateMessage")
    @Expose
    private boolean stateMessage;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("displayState")
    @Expose
    private String displayState;
    @SerializedName("createDate")
    @Expose
    private String createDate;
    @SerializedName("state")
    @Expose
    private String state;
    public final static Creator<OrderDetailTransaction> CREATOR = new Creator<OrderDetailTransaction>() {


        @SuppressWarnings({
                "unchecked"
        })
        public OrderDetailTransaction createFromParcel(Parcel in) {
            return new OrderDetailTransaction(in);
        }

        public OrderDetailTransaction[] newArray(int size) {
            return (new OrderDetailTransaction[size]);
        }

    };

    protected OrderDetailTransaction(Parcel in) {
        this.stateMessage = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.reference = ((String) in.readValue((String.class.getClassLoader())));
        this.displayState = ((String) in.readValue((String.class.getClassLoader())));
        this.createDate = ((String) in.readValue((String.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
    }

    public OrderDetailTransaction() {
    }

    public boolean isStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(boolean stateMessage) {
        this.stateMessage = stateMessage;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDisplayState() {
        return displayState;
    }

    public void setDisplayState(String displayState) {
        this.displayState = displayState;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(stateMessage);
        dest.writeValue(reference);
        dest.writeValue(displayState);
        dest.writeValue(createDate);
        dest.writeValue(state);
    }

    public int describeContents() {
        return 0;
    }

}
