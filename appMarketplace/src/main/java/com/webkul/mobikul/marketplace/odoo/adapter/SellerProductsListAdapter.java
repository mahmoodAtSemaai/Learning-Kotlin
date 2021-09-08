package com.webkul.mobikul.marketplace.odoo.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.webkul.mobikul.marketplace.odoo.databinding.ItemSellerProductListBinding;
import com.webkul.mobikul.marketplace.odoo.model.SellerProductListItem;

import java.util.List;

/**
 * Created by aastha.gupta on 1/3/18 in Mobikul_Odoo_Application.
 */

public class SellerProductsListAdapter extends RecyclerView.Adapter<SellerProductsListAdapter.ViewHolder> {

    private Context mContext;
    private List<SellerProductListItem> sellerProducts;

    public SellerProductsListAdapter(Context mContext, List<SellerProductListItem> sellerProducts) {
        this.mContext = mContext;
        this.sellerProducts = sellerProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemSellerProductListBinding binding = ItemSellerProductListBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SellerProductListItem product = sellerProducts.get(position);
        holder.mBinding.setData(product);
    }


    @Override
    public int getItemCount() {
        return sellerProducts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSellerProductListBinding mBinding;

        private ViewHolder(ItemSellerProductListBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;

        }
    }
}
