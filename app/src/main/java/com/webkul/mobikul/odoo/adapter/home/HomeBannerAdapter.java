package com.webkul.mobikul.odoo.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemViewPagerHomeBannerBinding;
import com.webkul.mobikul.odoo.handler.home.HomeBannerHandler;
import com.webkul.mobikul.odoo.model.generic.BannerImageData;

import java.util.List;


public class HomeBannerAdapter extends PagerAdapter {

    private final Context mContext;
    private List<BannerImageData> mBannerImageDatas;
    ViewPager viewPager;
    List<BannerImageData> list;

    public HomeBannerAdapter(Context context, List<BannerImageData> bannerImageDatas, ViewPager viewPager) {
        mContext = context;
        mBannerImageDatas = bannerImageDatas;
        this.viewPager = viewPager;
        list = mBannerImageDatas;
    }


    @Override
    public int getCount() {
        return mBannerImageDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemViewPagerHomeBannerBinding itemViewPagerBannerBinding = DataBindingUtil.bind(inflater.inflate(R.layout.item_view_pager_home_banner, container, false));
        itemViewPagerBannerBinding.setData(mBannerImageDatas.get(position));
        itemViewPagerBannerBinding.setHandler(new HomeBannerHandler(mContext, mBannerImageDatas.get(position)));
        itemViewPagerBannerBinding.executePendingBindings();
        container.addView(itemViewPagerBannerBinding.getRoot());
        if (position == mBannerImageDatas.size() - 1) {
            viewPager.post(runnable);
        }
        return (itemViewPagerBannerBinding.getRoot());
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mBannerImageDatas.addAll(list);
            notifyDataSetChanged();
        }
    };
}
