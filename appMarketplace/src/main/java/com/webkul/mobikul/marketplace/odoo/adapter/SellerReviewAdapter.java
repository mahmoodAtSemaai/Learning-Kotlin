package com.webkul.mobikul.marketplace.odoo.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.webkul.mobikul.marketplace.odoo.databinding.ItemSellerReviewBinding;
import com.webkul.mobikul.marketplace.odoo.handler.SellerReviewItemHandler;
import com.webkul.mobikul.marketplace.odoo.model.SellerReview;
import com.webkul.mobikul.odoo.model.ReviewLikeDislikeResponse;

import java.util.List;

/**
 * Created by aastha.gupta on 1/11/17 in Mobikul_Odoo_Application.
 */

public class SellerReviewAdapter extends RecyclerView.Adapter<SellerReviewAdapter.ViewHolder> {


    private Context mContext;
    private List<SellerReview> sellerReviews;

    public SellerReviewAdapter(Context mContext, List<SellerReview> sellerReviews) {
        this.mContext = mContext;
        this.sellerReviews = sellerReviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSellerReviewBinding binding = ItemSellerReviewBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setData(sellerReviews.get(position));
        holder.binding.setHandler(new SellerReviewItemHandler(mContext, this, position));
    }

    @Override
    public int getItemCount() {
        return sellerReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemSellerReviewBinding binding;

        ViewHolder(ItemSellerReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateListItem(ReviewLikeDislikeResponse reviewLikeDislikeResponse, int intListPos) {
        SellerReview sellerReview = sellerReviews.get(intListPos);
        sellerReview.setHelpful(reviewLikeDislikeResponse.getSellerReview().getHelpful());
        sellerReview.setNotHelpful(reviewLikeDislikeResponse.getSellerReview().getNotHelpful());
        notifyDataSetChanged();
    }
}
