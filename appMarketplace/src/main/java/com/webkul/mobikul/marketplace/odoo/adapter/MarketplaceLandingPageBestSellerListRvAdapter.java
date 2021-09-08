package com.webkul.mobikul.marketplace.odoo.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.webkul.mobikul.marketplace.odoo.databinding.ItemMarketplaceLandingPageSellerBinding;
import com.webkul.mobikul.marketplace.odoo.handler.MarketplaceLandingActivityHandler;
import com.webkul.mobikul.marketplace.odoo.model.SellerInfo;
import com.webkul.mobikul.odoo.adapter.home.ProductDefaultStyleRvAdapter;
import com.webkul.mobikul.odoo.model.generic.ProductData;

import java.util.ArrayList;
import java.util.List;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.SLIDER_MODE_DEFAULT;

/**
 * Created by aastha.gupta on 2/11/17 in Mobikul_Odoo_Application.
 */

public class MarketplaceLandingPageBestSellerListRvAdapter extends RecyclerView.Adapter<MarketplaceLandingPageBestSellerListRvAdapter.ViewHolder> {

    private Context mContext;
    private List<SellerInfo> sellerList;

    public MarketplaceLandingPageBestSellerListRvAdapter(Context mContext, List<SellerInfo> sellerList) {
        this.mContext = mContext;
        this.sellerList = sellerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMarketplaceLandingPageSellerBinding binding = ItemMarketplaceLandingPageSellerBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(sellerList.get(position));
        holder.binding.setHandler(new MarketplaceLandingActivityHandler(mContext));
        holder.binding.sellerProductRv.setAdapter(new ProductDefaultStyleRvAdapter(mContext, (ArrayList<ProductData>) sellerList.get(position).getSellerProducts().getProducts(), SLIDER_MODE_DEFAULT));
    }

    @Override
    public int getItemCount() {
        return sellerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ItemMarketplaceLandingPageSellerBinding binding;

        public ViewHolder(ItemMarketplaceLandingPageSellerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
