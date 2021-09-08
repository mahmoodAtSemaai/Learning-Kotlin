package com.webkul.mobikul.odoo.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.custom.TouchImageView;
import com.webkul.mobikul.odoo.databinding.FragmentFullScreenImageScrollBinding;
import com.webkul.mobikul.odoo.helper.ImageHelper;

import java.util.ArrayList;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CURRENT_ITEM_POSITION;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_IMAGES;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class FullScreenImageScrollFragment extends BaseFragment {

    private FragmentFullScreenImageScrollBinding mBinding;
    private ArrayList<String> mImages;

    public static FullScreenImageScrollFragment newInstance(int position, ArrayList<String> images) {

        Bundle args = new Bundle();
        args.putInt(BUNDLE_KEY_CURRENT_ITEM_POSITION, position);
        args.putStringArrayList(BUNDLE_KEY_IMAGES, images);
        FullScreenImageScrollFragment fragment = new FullScreenImageScrollFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_full_screen_image_scroll, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImages = getArguments().getStringArrayList(BUNDLE_KEY_IMAGES);
        mBinding.fullScreenProductViewPager.setAdapter(new TouchImageAdapter());
        int currentPosition = getArguments().getInt(BUNDLE_KEY_CURRENT_ITEM_POSITION, 0);
        mBinding.fullScreenProductViewPager.setCurrentItem(currentPosition);
    }

    private class TouchImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            TouchImageView touchImageView = new TouchImageView(container.getContext());
            ImageHelper.load(touchImageView, mImages.get(position), AppCompatResources.getDrawable(getContext(), R.drawable.ic_vector_odoo_placeholder_grey400_48dp), DiskCacheStrategy.AUTOMATIC, false, null);
            container.addView(touchImageView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return touchImageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
