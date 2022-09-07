package com.webkul.mobikul.odoo.adapter.loyalty

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemLoyaltyTransactionBinding
import com.webkul.mobikul.odoo.handler.chat.ChatHistoryListItemHandler
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyTransactionData

class LoyaltyHistoryListAdapter(
    private val context: Context,
    private val loyaltyHistoryList: List<LoyaltyTransactionData>
) :
    RecyclerView.Adapter<LoyaltyHistoryListAdapter.LoyaltyHistoryListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): LoyaltyHistoryListViewHolder {
        val inflater = LayoutInflater.from(context)
        val transactionView: View = inflater.inflate(R.layout.item_loyalty_transaction, parent, false)
        return LoyaltyHistoryListViewHolder(ItemLoyaltyTransactionBinding.bind(transactionView))
    }

    override fun onBindViewHolder(holder: LoyaltyHistoryListViewHolder, position: Int) {
        val loyaltyHistoryItem = loyaltyHistoryList[position]
        holder.mBinding.data = loyaltyHistoryItem
    }

    override fun getItemCount(): Int {
        return loyaltyHistoryList.size
    }

    class LoyaltyHistoryListViewHolder(val mBinding: ItemLoyaltyTransactionBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

    }
}