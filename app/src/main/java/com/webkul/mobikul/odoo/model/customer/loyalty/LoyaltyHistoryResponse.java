package com.webkul.mobikul.odoo.model.customer.loyalty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LoyaltyHistoryResponse {

    @SerializedName("total_count")
    @Expose
    private int totalCount;

    @SerializedName("loyalty_transactions")
    @Expose
    private List<LoyaltyTransactionData> loyaltyTransactions = null;

    public List<LoyaltyTransactionData> getLoyaltyTransactions() {
        if (loyaltyTransactions == null) {
            return new ArrayList<>();
        }
        return loyaltyTransactions;
    }

    public int getTotalCount() {
        return totalCount;
    }

}
