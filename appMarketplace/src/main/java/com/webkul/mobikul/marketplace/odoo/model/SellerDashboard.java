package com.webkul.mobikul.marketplace.odoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aastha.gupta on 20/2/18 in Mobikul_Odoo_Application.
 */

public class SellerDashboard {
    @SerializedName("pending_productCount")
    @Expose
    private int pendingProductCount;
    @SerializedName("approved_solCount")
    @Expose
    private int approved_solCount;
    @SerializedName("rejected_productCount")
    @Expose
    private int rejectedProductCount;
    @SerializedName("approved_productCount")
    @Expose
    private int approvedProductCount;
    @SerializedName("balance")
    @Expose
    private OrderAmount balance;
    @SerializedName("total")
    @Expose
    private OrderAmount total;
    @SerializedName("shipped_solCount")
    @Expose
    private int shipped_solCount;
    @SerializedName("new_solCount")
    @Expose
    private int new_solCount;
    @SerializedName("toBeApprovedPaymentCount")
    @Expose
    private int toBeApprovedPaymentCount;
    @SerializedName("approvedPaymentCount")
    @Expose
    private int approvedPaymentCount;
    @SerializedName("requestedPaymentCount")
    @Expose
    private int requestedPaymentCount;


    public int getPendingProductCount() {
        return pendingProductCount;
    }

    public int getRejectedProductCount() {
        return rejectedProductCount;
    }

    public int getApprovedProductCount() {
        return approvedProductCount;
    }

    public OrderAmount getBalance() {
        return balance;
    }

    public OrderAmount getTotal() {
        return total;
    }

    public int getApproved_solCount() {
        return approved_solCount;
    }

    public int getShipped_solCount() {
        return shipped_solCount;
    }

    public int getNew_solCount() {
        return new_solCount;
    }


    public int getToBeApprovedPaymentCount() {
        return toBeApprovedPaymentCount;
    }

    public int getApprovedPaymentCount() {
        return approvedPaymentCount;
    }

    public int getRequestedPaymentCount() {
        return requestedPaymentCount;
    }
}
