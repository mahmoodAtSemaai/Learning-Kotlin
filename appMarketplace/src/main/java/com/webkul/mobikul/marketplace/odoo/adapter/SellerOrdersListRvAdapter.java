package com.webkul.mobikul.marketplace.odoo.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.webkul.mobikul.marketplace.odoo.databinding.ItemSellerOrderListBinding;
import com.webkul.mobikul.marketplace.odoo.handler.SellerOrderListActivityHandler;
import com.webkul.mobikul.marketplace.odoo.model.SellerOrderLine;

import java.util.List;

/**
 * Created by aastha.gupta on 23/2/18 in Mobikul_Odoo_Application.
 */

public class SellerOrdersListRvAdapter extends RecyclerView.Adapter<SellerOrdersListRvAdapter.ViewHolder> {

    private final Context mContext;
    private final List<SellerOrderLine> mOrderLine;

    public SellerOrdersListRvAdapter(Context context, @NonNull List<SellerOrderLine> orderLine) {
        mContext = context;
        mOrderLine = orderLine;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemSellerOrderListBinding binding = ItemSellerOrderListBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SellerOrderLine orderLine = mOrderLine.get(position);
        holder.mBinding.setData(orderLine);
        holder.mBinding.setHandler(new SellerOrderListActivityHandler(mContext, orderLine));
    }


    @Override
    public int getItemCount() {
        return mOrderLine.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSellerOrderListBinding mBinding;

        private ViewHolder(ItemSellerOrderListBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;

        }
    }
}
