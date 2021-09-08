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
public class DeliveryOrder implements Parcelable {

    @SerializedName("displayState")
    @Expose
    private String displayState;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("reportUrl")
    @Expose
    private String reportUrl;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Creator<DeliveryOrder> CREATOR = new Creator<DeliveryOrder>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DeliveryOrder createFromParcel(Parcel in) {
            return new DeliveryOrder(in);
        }

        public DeliveryOrder[] newArray(int size) {
            return (new DeliveryOrder[size]);
        }

    };

    protected DeliveryOrder(Parcel in) {
        this.displayState = ((String) in.readValue((String.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.reportUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DeliveryOrder() {
    }

    public String getDisplayState() {
        return displayState;
    }

    public void setDisplayState(String displayState) {
        this.displayState = displayState;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(displayState);
        dest.writeValue(state);
        dest.writeValue(date);
        dest.writeValue(reportUrl);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}
