package com.webkul.mobikul.odoo.adapter.product;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemViewPagerProductImageBinding;
import com.webkul.mobikul.odoo.handler.product.ProductImageHandler;

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
public class ProductImageAdapter extends PagerAdapter {
    @SuppressWarnings("unused")
    private static final String TAG = "ProductSliderAdapter";
    private final Context mContext;
    private final List<String> mImages;


    public ProductImageAdapter(Context context, List<String> images) {
        mContext = context;
        mImages = images;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemViewPagerProductImageBinding itemViewPagerProductImageBinding = DataBindingUtil.bind(inflater.inflate(R.layout.item_view_pager_product_image, container, false));
        itemViewPagerProductImageBinding.setImageUrl(mImages.get(position));
        itemViewPagerProductImageBinding.setHandler(new ProductImageHandler(mContext, mImages, position));
        itemViewPagerProductImageBinding.executePendingBindings();
        container.addView(itemViewPagerProductImageBinding.getRoot());
        return (itemViewPagerProductImageBinding.getRoot());
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}

