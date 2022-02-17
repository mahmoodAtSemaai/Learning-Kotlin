package com.webkul.mobikul.odoo.adapter.extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemSearchSuggestionProductBinding;
import com.webkul.mobikul.odoo.handler.extra.search.SearchSuggestionProductHandler;
import com.webkul.mobikul.odoo.helper.SearchHelper;
import com.webkul.mobikul.odoo.model.generic.ProductData;

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

public class SearchSuggestionProductAdapter extends RecyclerView.Adapter<SearchSuggestionProductAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "SearchSuggestionProduct";

    private final Context mContext;
    private List<ProductData> mProducts;
    private String mSearchQuery;

    public SearchSuggestionProductAdapter(Context context, List<ProductData> products, String searchQuery) {
        mContext = context;
        mProducts = products;
        mSearchQuery = searchQuery;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_search_suggestion_product, parent, false);
        return new ViewHolder(contactView);
    }


    public void updateData(List<ProductData> products, String searchQuery) {
        this.mProducts = products;
        mSearchQuery = searchQuery;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int indexOfMatch = mProducts.get(position).getName().toLowerCase().indexOf(mSearchQuery.toLowerCase());
        if (!mSearchQuery.isEmpty() && indexOfMatch > -1) {
            mProducts.get(position).setName(SearchHelper.getHighlightedString(mProducts.get(position).getName(), mSearchQuery, indexOfMatch));
        }
        holder.mBinding.setData(mProducts.get(position));
        holder.mBinding.setHandler(new SearchSuggestionProductHandler(mContext, mProducts.get(position)));
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSearchSuggestionProductBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
