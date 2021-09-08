package com.webkul.mobikul.odoo.adapter.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemOrderDetailTransactionsBinding;
import com.webkul.mobikul.odoo.model.customer.order.OrderDetailTransaction;

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
public class OrderDetailTransactionRvAdapter extends RecyclerView.Adapter<OrderDetailTransactionRvAdapter.ViewHolder> {
    private final Context mContext;
    private final List<OrderDetailTransaction> mTransactionList;

    public OrderDetailTransactionRvAdapter(Context mContext, List<OrderDetailTransaction> mTransactionList) {
        this.mContext = mContext;
        this.mTransactionList = mTransactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(mContext);
        View transactionView = inflater.inflate(R.layout.item_order_detail_transactions,parent,false);
        return new ViewHolder(transactionView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mBinding.setData(mTransactionList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mTransactionList == null){
            return 0;
        }
        return mTransactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ItemOrderDetailTransactionsBinding mBinding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
