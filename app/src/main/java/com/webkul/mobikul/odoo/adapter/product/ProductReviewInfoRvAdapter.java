package com.webkul.mobikul.odoo.adapter.product;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemProductReviewInfoBinding;
import com.webkul.mobikul.odoo.handler.product.ProductReviewInfoHandler;
import com.webkul.mobikul.odoo.model.product.Review;

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
public class ProductReviewInfoRvAdapter extends RecyclerView.Adapter<ProductReviewInfoRvAdapter.Holder> {

    private Context mContext;
    private List<Review> mReviewList;

    public ProductReviewInfoRvAdapter(Context context, List<Review> reviewList) {
        mContext = context;
        mReviewList = reviewList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_product_review_info, parent, false);
        return new Holder(contactView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mBinding.setData(mReviewList.get(position));
        holder.mBinding.setHandler(new ProductReviewInfoHandler(mContext, mReviewList.get(position)));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        private final ItemProductReviewInfoBinding mBinding;

        Holder(View v) {
            super(v);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
