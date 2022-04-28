package com.webkul.mobikul.odoo.adapter.home;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemFeaturedCategoryHomeBinding;
import com.webkul.mobikul.odoo.model.generic.FeaturedCategoryData;

import java.util.List;


public class FeaturedCategoriesAdapter extends RecyclerView.Adapter<FeaturedCategoriesAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "FeaturedCategoriesRvAda";
    private FeaturedCategoryDataValue featuredCategoryDataValue;
    private int selectedPos = 0;
    private boolean firstTimeSetup = true;


    private final Context mContext;
    private final List<FeaturedCategoryData> mFeaturedCategoryDatas;

    public FeaturedCategoriesAdapter(Context context, List<FeaturedCategoryData> featuredCategotyDatas,
                                     FeaturedCategoryDataValue featuredCategoryDataValue) {
        mContext = context;
        mFeaturedCategoryDatas = featuredCategotyDatas;
        this.featuredCategoryDataValue = featuredCategoryDataValue;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_featured_category_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FeaturedCategoryData featuredCategotyData = mFeaturedCategoryDatas.get(position);

        holder.mBinding.setData(featuredCategotyData);

        holder.itemView.findViewById(R.id.container).setBackgroundResource(selectedPos == position ?
                R.drawable.featured_categories_selected_item_bg : Color.TRANSPARENT);


        holder.mBinding.categoryName.setTextColor(selectedPos == position ? ContextCompat.getColor(mContext, R.color.background_orange) : ContextCompat.getColor(mContext, R.color.black));
        if(firstTimeSetup) {
            featuredCategoryDataValue.data(featuredCategotyData , position);
        }
        firstTimeSetup = false;
        holder.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                featuredCategoryDataValue.data(featuredCategotyData , holder.getAdapterPosition());
                notifyItemChanged(selectedPos);
                selectedPos = holder.getLayoutPosition();
                notifyItemChanged(selectedPos);
            }
        });
        holder.mBinding.executePendingBindings();
        holder.itemView.setSelected(selectedPos == position);
    }


    @Override
    public int getItemCount() {
        return mFeaturedCategoryDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemFeaturedCategoryHomeBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    public interface FeaturedCategoryDataValue {
        void data(FeaturedCategoryData featuredCategoryData , Integer pos);
    }
}