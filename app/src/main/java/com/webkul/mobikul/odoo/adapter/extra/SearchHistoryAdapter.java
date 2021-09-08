package com.webkul.mobikul.odoo.adapter.extra;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemSearchHistoryBinding;
import com.webkul.mobikul.odoo.handler.extra.search.SearchHistoryHandler;
import com.webkul.mobikul.odoo.helper.SearchHelper;

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
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "SearchSuggestionProduct";

    private final Context mContext;
    private List<String> mSearchQueries;
    private String mSearchQuery;

    public SearchHistoryAdapter(Context context, List<String> searchQueries, String searchQuery) {
        mContext = context;
        mSearchQueries = searchQueries;
        mSearchQuery = searchQuery;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(contactView);
    }

    public void updateSearchHistory(List<String> searchQueries, String searchQuery) {
        mSearchQueries = searchQueries;
        mSearchQuery = searchQuery;
        notifyDataSetChanged();
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {
        int indexOfMatch = mSearchQueries.get(position).toLowerCase().indexOf(mSearchQuery.toLowerCase());
        if (!mSearchQuery.isEmpty() && indexOfMatch > -1) {
            holder.mBinding.setQuery(SearchHelper.getHighlightedString(mSearchQueries.get(position), mSearchQuery, indexOfMatch));
        } else {
            holder.mBinding.setQuery(mSearchQueries.get(position));
        }
        holder.mBinding.setHandler(new SearchHistoryHandler(mContext, mSearchQueries.get(position)));
    }

    @Override
    public int getItemCount() {
        return mSearchQueries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemSearchHistoryBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
