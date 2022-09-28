package com.webkul.mobikul.odoo.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.BannerEntity
import com.webkul.mobikul.odoo.databinding.ItemViewPagerHomeBannerV1Binding
import com.webkul.mobikul.odoo.helper.ImageHelper

class HomeBannerAdapterV1(
        private val banners: List<BannerEntity>,
        private val listener: HomeBannerAdapterListener
) : PagerAdapter() {


    override fun getCount(): Int = banners.size


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val binding: ItemViewPagerHomeBannerV1Binding = DataBindingUtil.bind(LayoutInflater.from(container.context).inflate(R.layout.item_view_pager_home_banner_v1, container, false))!!

        ImageHelper.load(
                binding.ivBanner,
                banners[position].url,
                ContextCompat.getDrawable(container.context, R.drawable.ic_vector_odoo_placeholder_grey400_48dp),
                DiskCacheStrategy.NONE,
                true,
                ImageHelper.ImageType.BANNER_SIZE_GENERIC
        )

        binding.ivBanner.setOnClickListener {
            listener.onBannerClick(banners[position])
        }

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    interface HomeBannerAdapterListener {
        fun onBannerClick(bannerEntity: BannerEntity)
    }

}

