package com.webkul.mobikul.odoo.adapter.customer;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemWishlistProductInfoBinding;
import com.webkul.mobikul.odoo.handler.customer.WishlistProductInfoItemHandler;
import com.webkul.mobikul.odoo.model.customer.wishlist.WishListData;

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
public class WishlistProductInfoRvAdapter extends RecyclerView.Adapter<WishlistProductInfoRvAdapter.Holder> {

    private Context mContext;
    private List<WishListData> mWishLists;

    public WishlistProductInfoRvAdapter(Context context, List<WishListData> wishLists) {
        mContext = context;
        mWishLists = wishLists;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_wishlist_product_info, parent, false);
        return new Holder(contactView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mBinding.setData(mWishLists.get(position));
        holder.mBinding.setHandler(new WishlistProductInfoItemHandler(mContext, mWishLists.get(position)));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mWishLists.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        private final ItemWishlistProductInfoBinding mBinding;

        Holder(View v) {
            super(v);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
