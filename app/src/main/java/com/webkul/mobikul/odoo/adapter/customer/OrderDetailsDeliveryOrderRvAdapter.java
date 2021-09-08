package com.webkul.mobikul.odoo.adapter.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemOrderDetailsDeliveryOrderBinding;
import com.webkul.mobikul.odoo.handler.order.OrderFragmentHandler;
import com.webkul.mobikul.odoo.model.customer.order.DeliveryOrder;

import java.util.List;
/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class OrderDetailsDeliveryOrderRvAdapter extends RecyclerView.Adapter<OrderDetailsDeliveryOrderRvAdapter.ViewHolder> {
    private final Context mContext;
    private final List<DeliveryOrder> mTransactionList;


    public OrderDetailsDeliveryOrderRvAdapter(Context mContext, List<DeliveryOrder> mTransactionList) {
        this.mContext = mContext;
        this.mTransactionList = mTransactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View deliveryOrderView = inflater.inflate(R.layout.item_order_details_delivery_order, parent, false);
        return new ViewHolder(deliveryOrderView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mBinding.setData(mTransactionList.get(position));
        holder.mBinding.setHandler(new OrderFragmentHandler(mContext));
    }

    @Override
    public int getItemCount() {
        if (mTransactionList== null){
            return 0;
        }
        return mTransactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ItemOrderDetailsDeliveryOrderBinding mBinding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
