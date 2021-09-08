package com.webkul.mobikul.odoo.adapter.cart;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemBagBinding;
import com.webkul.mobikul.odoo.handler.bag.BagItemsRecyclerHandler;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.cart.BagItemData;

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
public class BagItemsRecyclerAdapter extends RecyclerView.Adapter<BagItemsRecyclerAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "CartItemsRecyclerAdapte";

    private final Context mContext;
    private final List<BagItemData> mItems;

    public BagItemsRecyclerAdapter(Context context, List<BagItemData> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_bag, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BagItemData cartItem = mItems.get(position);
        holder.mBinding.setData(cartItem);
        holder.mBinding.setIsLoggedIn(AppSharedPref.isLoggedIn(mContext));
        holder.mBinding.setWishlistEnabled(AppSharedPref.isAllowedWishlist(mContext));
        holder.mBinding.setHandler(new BagItemsRecyclerHandler(mContext, cartItem));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBagBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

}
