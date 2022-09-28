package com.webkul.mobikul.odoo.adapter.extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.data.entity.ProductEntity;
import com.webkul.mobikul.odoo.databinding.ItemSearchSuggestionProductBinding;
import com.webkul.mobikul.odoo.handler.extra.search.SearchSuggestionProductHandler;
import com.webkul.mobikul.odoo.helper.SearchHelper;
import com.webkul.mobikul.odoo.model.generic.ProductData;

import java.util.List;


public class SearchSuggestionProductAdapter extends RecyclerView.Adapter<SearchSuggestionProductAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "SearchSuggestionProduct";

    private final Context context;
    private List<ProductEntity> products;
    private String searchQuery;

    public SearchSuggestionProductAdapter(Context context, List<ProductEntity> products, String searchQuery) {
        this.context = context;
        this.products = products;
        this.searchQuery = searchQuery;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_search_suggestion_product, parent, false);
        return new ViewHolder(contactView);
    }


    public void updateData(List<ProductEntity> products, String searchQuery) {
        this.products = products;
        this.searchQuery = searchQuery;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int indexOfMatch = products.get(position).getName().toLowerCase().indexOf(searchQuery.toLowerCase());
        if (!searchQuery.isEmpty() && indexOfMatch > -1) {
            products.get(position).setName(SearchHelper.getHighlightedString(products.get(position).getName(), searchQuery, indexOfMatch));
        }
        holder.binding.setData(products.get(position));
        holder.binding.setHandler(new SearchSuggestionProductHandler(context, products.get(position)));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSearchSuggestionProductBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
