package com.webkul.mobikul.odoo.model.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants;

public class VillageData {
    private static final String TAG = "VillageData";

    @SerializedName(AddressAPIConstants.ID)
    @Expose
    private int id;
    @SerializedName(AddressAPIConstants.NAME)
    @Expose
    private String name;
    @SerializedName(AddressAPIConstants.ZIP)
    @Expose
    private String zip;
    @SerializedName(AddressAPIConstants.AVAILABILITY)
    @Expose
    private boolean is_available;

    public static String getTAG() {
        return TAG;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public boolean isIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }

    public String getZip() {
        if (zip == null) {
            return "";
        }
        return zip;
    }

}
