package com.webkul.mobikul.odoo.adapter.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.data.response.models.WishListData;
import com.webkul.mobikul.odoo.databinding.ItemWishlistProductInfoBinding;
import com.webkul.mobikul.odoo.handler.customer.WishlistProductInfoItemHandler;

import java.util.List;

public class WishlistProductInfoRvAdapter extends RecyclerView.Adapter<WishlistProductInfoRvAdapter.Holder> {

    private Context mContext;
    private List<WishListData> mWishLists;
    public WishListInterface wishListInterface;

    public WishlistProductInfoRvAdapter(Context context, List<WishListData> wishLists, WishListInterface wishListInterface) {
        mContext = context;
        mWishLists = wishLists;
        this.wishListInterface = wishListInterface;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_wishlist_product_info, parent, false);
        return new Holder(contactView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.binding.setData(mWishLists.get(position));
        WishlistProductInfoItemHandler handler = new WishlistProductInfoItemHandler(mContext, mWishLists.get(position));
        holder.binding.setHandler(handler);
        holder.binding.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishListInterface.onDeleteProduct(holder.getAdapterPosition());
            }
        });

        holder.binding.addProductToBagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishListInterface.addProductToCart(holder.getAdapterPosition());
            }
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mWishLists.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        private final ItemWishlistProductInfoBinding binding;

        Holder(View v) {
            super(v);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public interface WishListInterface {
        void onDeleteProduct(Integer pos);

        void addProductToCart(Integer pos);
    }
}
