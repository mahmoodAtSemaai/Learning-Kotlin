package com.webkul.mobikul.odoo.adapter.loyalty

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemLoyaltyBannerBinding
import com.webkul.mobikul.odoo.handler.loyalty.LoyaltyTermsListItemHandler
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyTermsData

class LoyaltyTermsListAdapter(
    private val context: Context,
    private val loyaltyTermsList: List<LoyaltyTermsData>
) :
    RecyclerView.Adapter<LoyaltyTermsListAdapter.LoyaltyTermsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): LoyaltyTermsListViewHolder {
        val inflater = LayoutInflater.from(context)
        val termsView: View = inflater.inflate(R.layout.item_loyalty_banner, parent, false)
        return LoyaltyTermsListViewHolder(ItemLoyaltyBannerBinding.bind(termsView))
    }

    override fun onBindViewHolder(holder: LoyaltyTermsListViewHolder, position: Int) {
        val loyaltyTermsItem = loyaltyTermsList[position]
        val loyaltyTermsListItemHandler =
            LoyaltyTermsListItemHandler(context, loyaltyTermsItem, holder.mBinding)
        loyaltyTermsListItemHandler.setView()
    }

    override fun getItemCount(): Int {
        return loyaltyTermsList.size
    }

    class LoyaltyTermsListViewHolder(val mBinding: ItemLoyaltyBannerBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

    }
}